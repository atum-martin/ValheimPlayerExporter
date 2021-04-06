
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.io.FileOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ValheimFileFormat {

	public static void main(String[] args) {
		if (args.length >= 1 && args[0].equalsIgnoreCase("import")){
			String playerName = args[1];
			//imports a json file and writes a Valheim player file (fch)
			importValheimPlayer(playerName+".json", playerName+".fch");
			copyFchToDir(playerName, playerName+".fch");
		} else {
			//reads a Valheim player file and writes a json output
			String inputFileStr = getInputFchFile();
			if(inputFileStr == null) {
				System.out.println("Could not resolve path to fch file. A file should exist in the local directory called input.fch. ");
				System.out.println("Windows path:  %appdata%/../LocalLow/IronGate/Valheim/characters");
			}
			System.out.println("Using input file: "+inputFileStr);
			ValheimFileFormat file = new ValheimFileFormat(inputFileStr);
			file.exportValheimPlayer(file.getJsonFilename());
		}
	}
	
	private static final int MAX_BUFFER_SIZE = 50*1024*1024;
	private ValheimCharacter character;
	private String filePath = null;
	
	public ValheimFileFormat() {
		
	}
	
	private static void copyFchToDir(String playerName, String localFile) {
		Date date = Calendar.getInstance().getTime();  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmm");  
        String dateStr = dateFormat.format(date);  
		File currentSaveFile = new File(System.getenv("APPDATA")+"/../LocalLow/IronGate/Valheim/characters/"+playerName+".fch");
		File backupFile = new File(System.getenv("APPDATA")+"/../LocalLow/IronGate/Valheim/characters/"+playerName+"_"+dateStr+".fch");
		File newLocalFile = new File(localFile);
		try {
			//backup the old save file
			Files.move(currentSaveFile.getAbsoluteFile().toPath(), backupFile.getAbsoluteFile().toPath());
			//copy in the updated file
			Files.copy(newLocalFile.getAbsoluteFile().toPath(), currentSaveFile.getAbsoluteFile().toPath());
		} catch (IOException e) {
			System.out.println("error updating the valheim player file in games save directory.");
			e.printStackTrace();
		}
	}

	private static String getInputFchFile() {
		File f = new File("./input.fch");
		if(f.exists())
			return f.getAbsolutePath();
		File dir = new File(System.getenv("APPDATA")+"/../LocalLow/IronGate/Valheim/characters");
		if(!dir.exists()) return null;
		String returnFile = null;
		long lastModified = -1L;
		for(File file : dir.listFiles()) {
			if(file.getName().endsWith(".fch") && file.lastModified() > lastModified) {
				returnFile = file.getAbsolutePath();
				lastModified = file.lastModified();
			}
		}
		return returnFile;
	}
	
	public String getJsonFilename() {
		String outputFile = (new File(filePath)).getName();
		outputFile = outputFile.substring(0, outputFile.length()-4)+".json";
		return outputFile;
	}

	public void exportValheimPlayer(String outputFileName) {
		File f = new File("./worlds");
		f.mkdir();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(character);
		writeFile(outputFileName, json.getBytes());
		for(ValheimWorld world : character.worlds) {
			writeFile("./worlds/"+world.getWorldId()+".bin", world.getMapData());
		}
	}
	
	public static ValheimFileFormat importValheimPlayer(String jsonFileName, String writeValheimFilename) {
		ValheimFileFormat file = new ValheimFileFormat();
		
		try {
			String json = readFile(jsonFileName);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			file.character = gson.fromJson(json, ValheimCharacter.class);
			file.importWorldFiles();
		
			System.out.println(file.character);
			file.writeToValheimFormatFile(writeValheimFilename, file.character);
			
		} catch (IOException e) {
			System.out.println("error during importing valheim file");
			e.printStackTrace();
		}
		return file;
	}

	private void importWorldFiles() throws IOException {
		for(ValheimWorld world : character.worlds) {
			ByteBuffer buffer = bytebufFromFile("./worlds/"+world.getWorldId()+".bin");
            
            byte[] mapData = new byte[buffer.limit()];
            buffer.get(mapData, 0, mapData.length);
            
            world.setMapData(mapData);
		}
		
	}
	
	private static ByteBuffer bytebufFromFile(String fileName) throws IOException {
		RandomAccessFile valFile = new RandomAccessFile(fileName,"r");
        FileChannel inChannel = valFile.getChannel();
        long fileSize = inChannel.size();
        ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
        inChannel.read(buffer);
        buffer.flip();
        valFile.close();
        return buffer;
	}

	private static String readFile(String fileName) throws IOException {
		ByteBuffer buffer = bytebufFromFile(fileName);
        
        byte[] strData = new byte[buffer.limit()];
        buffer.get(strData, 0, strData.length);
		return new String(strData);
	}

	private static void writeFile(String fileName, byte[] bytes) {
		try {
			FileOutputStream bw = new FileOutputStream(fileName);
			bw.write(bytes);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public void writeToValheimFormatFile(String fileName, ValheimCharacter chara) {
		ByteBuffer buf = ByteBuffer.allocate(MAX_BUFFER_SIZE);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		writeToValheimFormat(buf, chara);
		FileChannel wChannel;
		try {
			wChannel = new FileOutputStream(new File(fileName), false).getChannel();
			ByteBuffer headerBuf = ByteBuffer.allocate(4);
			headerBuf.order(ByteOrder.LITTLE_ENDIAN);
			headerBuf.putInt(buf.limit());
			headerBuf.flip();
			
			ByteBuffer footer = computeShaFooter(buf);
			
			wChannel.write(headerBuf);
			wChannel.write(buf);
			wChannel.write(footer);
			wChannel.close();
		} catch (FileNotFoundException e) {
			System.out.println("file not found writing: "+fileName);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error writing file: "+fileName);
			e.printStackTrace();
		}
		
	}
	
	private ByteBuffer computeShaFooter(ByteBuffer buf) {
		ByteBuffer bb = ByteBuffer.allocate(buf.limit()).put(buf);
		bb.flip();
		
		byte[] data = new byte[bb.limit()];
        bb.get(data, 0, data.length);

        ByteBuffer out = ByteBuffer.allocate(68);
        out.order(ByteOrder.LITTLE_ENDIAN);
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			byte[] messageDigest = md.digest(data);
			out.putInt(64);
			out.put(messageDigest);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		bb.flip();
		out.flip();
		buf.flip();
		
		return out;
	}

	public void writeToValheimFormat(ByteBuffer buf, ValheimCharacter chara) {
		buf.putInt(chara.getVersion());
		buf.putInt(chara.getKills());
		buf.putInt(chara.getDeaths());
		buf.putInt(chara.getCrafts());
		buf.putInt(chara.getBuilds());
		buf.putInt(chara.getNumberOfWorlds());
		for(ValheimWorld world : chara.worlds) {
			
			buf.putLong(world.getWorldId());
			buf.put((byte) (world.isHasCustomSpawnPoint() ? 1 : 0));
			putPos(buf, world.getSpawnPoint());
			
			buf.put((byte) (world.isHasLogoutPoint() ? 1 : 0));
			putPos(buf, world.getLogoutPoint());
			
			buf.put((byte) (world.isHasDeathPoint() ? 1 : 0));
			putPos(buf, world.getDeathPoint());
			putPos(buf, world.getHomePoint());
			
			if(world.getMapData() == null) {
				buf.put((byte) 0);
			} else {
				buf.put((byte) 1);
				buf.putInt(world.getMapData().length);
				buf.put(world.getMapData());
			}
		}
		putStr(buf, chara.getName());
		buf.putLong(chara.getId());
		putStr(buf, chara.getStartSeed());
		
		if(chara.getDataVersion() == 0) {
			return;
		}
		buf.put((byte) 1);
		ByteBuffer buf2 = ByteBuffer.allocate(MAX_BUFFER_SIZE);
		buf2.order(ByteOrder.LITTLE_ENDIAN);
		writeToValheimFormatSub(buf2, chara);
		
		buf.putInt(buf2.limit());
		buf.put(buf2);
		buf.flip();
		
	}
	
	public void writeToValheimFormatSub(ByteBuffer buf, ValheimCharacter chara) {
		buf.putInt(chara.getDataVersion());
		buf.putFloat(chara.getMaxHp());
		buf.putFloat(chara.getHp());
		buf.putFloat(chara.getStamina());
		buf.put((byte) (chara.isFirstSpawn() ? 1 : 0));
		buf.putFloat(chara.getTimeSinceDeath());
		putStr(buf, chara.getGuardianPower());
		buf.putFloat(chara.getGuardianPowerCooldown());
		buf.putInt(chara.getInventoryVersion());
		
		buf.putInt(chara.inventory.size());
		for(ValheimItem item : chara.inventory) {
			putStr(buf, item.getName());
			buf.putInt(item.getStack());
			buf.putFloat(item.getDurability());
			buf.putInt(item.getPos().getX());
			buf.putInt(item.getPos().getY());
			buf.put((byte) (item.isEquipped() ? 1 : 0));
			buf.putInt(item.getQuality());
			buf.putInt(item.getVariant());
			buf.putLong(item.getCrafterId());
			putStr(buf, item.getCrafterName());
		}
		
		buf.putInt(chara.recipes.size());
		for(String s : chara.recipes) {
			putStr(buf, s);
		}
		
		buf.putInt(chara.stations.size());
		for(Entry<String,Integer> s : chara.stations) {
			putStr(buf, s.getKey());
			buf.putInt(s.getValue());
		}
		
		buf.putInt(chara.materials.size());
		for(String s : chara.materials) {
			putStr(buf, s);
		}
		
		buf.putInt(chara.tutorials.size());
		for(String s : chara.tutorials) {
			putStr(buf, s);
		}
		
		buf.putInt(chara.uniques.size());
		for(String s : chara.uniques) {
			putStr(buf, s);
		}
		
		buf.putInt(chara.trophies.size());
		for(String s : chara.trophies) {
			putStr(buf, s);
		}
		
		buf.putInt(chara.biomes.size());
		for(Integer i : chara.biomes) {
			buf.putInt(i);
		}
		
		buf.putInt(chara.texts.size());
		for(Entry<String,String> s : chara.texts) {
			putStr(buf, s.getKey());
			putStr(buf, s.getValue());
		}
		
		putStr(buf, chara.getBeard());
		putStr(buf, chara.getHair());
		putPos(buf, chara.getSkinColor());
		putPos(buf, chara.getHairColor());
		buf.putInt(chara.getGender());
		
		buf.putInt(chara.food.size());
		for(ValheimFood food : chara.food) {
			putStr(buf, food.getName());
			buf.putFloat(food.getHpLeft());
			buf.putFloat(food.getStaminaLeft());
		}
		
		buf.putInt(chara.getSkillsVersion());
		buf.putInt(chara.skills.size());
		for(ValheimSkill skill : chara.skills) {
			buf.putInt(skill.getSkillName());
			buf.putFloat(skill.getLevel());
			buf.putFloat(skill.getData());
		}
		buf.flip();
	}
	
	public ValheimFileFormat(String fileName) {
		filePath = fileName;
		try {
			RandomAccessFile valFile = new RandomAccessFile(fileName,"r");
            FileChannel inChannel = valFile.getChannel();
            long fileSize = inChannel.size();
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            
            inChannel.read(buffer);
            buffer.flip();
            valFile.close();
            character = new ValheimCharacter();
            buffer.getInt();
            character.setVersion(buffer.getInt());
            character.setKills(buffer.getInt());
            character.setDeaths(buffer.getInt());
            character.setCrafts(buffer.getInt());
            character.setBuilds(buffer.getInt());
            character.setNumberOfWorlds(buffer.getInt());
            
            for(int i = 0; i < character.getNumberOfWorlds(); i++) {
            	ValheimWorld world = new ValheimWorld(buffer.getLong());
            	world.setHasCustomSpawnPoint(buffer.get() == 1);
            	world.setSpawnPoint(readPos(buffer));
            	world.setHasLogoutPoint(buffer.get() == 1);
            	world.setLogoutPoint(readPos(buffer));
            	world.setHasDeathPoint(buffer.get() == 1);
            	world.setDeathPoint(readPos(buffer));
            	world.setHomePoint(readPos(buffer));
            	//additional map data
            	if(buffer.get() == 1) {
            		world.setMapData(readData(buffer));
            	} else {
            		world.setMapData(null);
            	}
            	character.addWorld(world);
            }
            character.setName(readStr(buffer));
            System.out.println("Character Name "+character.getName());
            character.setId(buffer.getLong());
            character.setStartSeed(readStr(buffer));
            //new characters
            if(buffer.get() == 0) {
            	System.out.println("ending new chara: "+buffer.remaining());
            	return;
            }
            
            @SuppressWarnings("unused")
			int dataLen = buffer.getInt();
            character.setDataVersion(buffer.getInt());
            character.setMaxHp(buffer.getFloat());
            character.setHp(buffer.getFloat());
            character.setStamina(buffer.getFloat());
            character.setFirstSpawn(buffer.get() == 1);
            character.setTimeSinceDeath(buffer.getFloat());
            character.setGuardianPower(readStr(buffer));
            character.setGuardianPowerCooldown(buffer.getFloat());
            
            character.setInventoryVersion(buffer.getInt());
            int numOfItems = buffer.getInt();
            
            for(int i = 0; i <  numOfItems; i++) {
            	ValheimItem item = new ValheimItem(
            			readStr(buffer),
            			buffer.getInt(),
            			buffer.getFloat(),
            			new ValheimItemPos(buffer.getInt(), buffer.getInt()),
            			buffer.get() == 1,
            			buffer.getInt(),
            			buffer.getInt(),
            			buffer.getLong(),
            			readStr(buffer)
            		);
            	character.addToInventory(item);
            			
            }
            
            int len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.addRecipe(readStr(buffer));
            
            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.addStation(readStr(buffer), buffer.getInt());

            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.knownMaterials(readStr(buffer));
            
            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.shownTutorials(readStr(buffer));
            
            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.addUniques(readStr(buffer));
            
            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.addTrophies(readStr(buffer));
            
            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.addBiome(buffer.getInt());
            
            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.addText(readStr(buffer), readStr(buffer));
            
            character.setBeard(readStr(buffer));
            character.setHair(readStr(buffer));
            character.setSkinColor(readPos(buffer));
            character.setHairColor(readPos(buffer));
            character.setGender(buffer.getInt());
            
            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.addFood(new ValheimFood(readStr(buffer), buffer.getFloat(), buffer.getFloat()));
            
            character.setSkillsVersion(buffer.getInt());
            len = buffer.getInt();
            for(int i = 0; i <  len; i++)
            	character.addSkill(new ValheimSkill(buffer.getInt(), buffer.getFloat(), buffer.getFloat()));
            
            byte[] extraData = new byte[buffer.remaining()];
            buffer.get(extraData, 0, len);
            character.setExtraData(extraData);
            
		} catch (FileNotFoundException e) {
			System.out.println("file not found: "+fileName);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error reading file: "+fileName);
			e.printStackTrace();
		}
	}
	
	public ValheimPos readPos(ByteBuffer buffer) {
		return new ValheimPos(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
	}
	
	public void putPos(ByteBuffer buffer, ValheimPos pos) {
		buffer.putFloat(pos.getX());
		buffer.putFloat(pos.getY());
		buffer.putFloat(pos.getZ());
	}
	
	public void putStr(ByteBuffer buffer, String str) {
		buffer.put((byte) str.length());
		buffer.put(str.getBytes());
	}
	
	public byte[] readData(ByteBuffer buffer) {
		
		int len = buffer.getInt();
		byte[] buf = new byte[len];
		buffer.get(buf, 0, len);
		return buf;
	}
	
	public String readStr(ByteBuffer buffer) {
		int len = buffer.get();
		if(len == -1) {
			return null;
		}
		byte[] buf = new byte[len];
		buffer.get(buf, 0, len);
		String str = new String(buf);
		return str;
	}

}
