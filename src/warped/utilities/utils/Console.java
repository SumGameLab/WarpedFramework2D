/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.MediaException;
import warped.application.state.WarpedFramework2D;
import warped.application.state.WarpedState;
import warped.functionalInterfaces.WarpedAction;

public abstract class Console {

	private static final String LOG_DIRECTORY = "dat/log";
	private static final String PREFIX = "/WarpedLog_";
	private static final String SUFFIX = "_wldat.txt";
	
	private static final String CONDITION = "Condition : ";
	private static final String ERROR = "Error : ";
	private static final String MET = "Met : ";
	private static final String LINE = "Line : ";
	private static final String BLUE = "Blue : ";
	private static final String PRINT = "		:  ";
	private static final String STACK = "StackTrace : ";
	
	public enum ConsoleColour {
		BLACK,  
		RED,    
		GREEN,  
		YELLOW, 
		BLUE,   
		PURPLE, 
		CYAN,   
		WHITE,
		;
	}
	
	private static final String ANSI_RESET  = "\u001B[0m";
	private static final String ANSI_BLACK  = "\u001B[30m";
	private static final String ANSI_RED    = "\u001B[31m";
	private static final String ANSI_GREEN  = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE   = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN   = "\u001B[36m";
	private static final String ANSI_WHITE  = "\u001B[37m";
		
	private static File logDir;
	private static File logFile;
	private static String logFilePath;
	
	private static List<String> logA = new ArrayList<>();
	private static List<String> logB = new ArrayList<>();
	
	private static int index = 0; 
	private static boolean isDirectoryValidated = false;
	private static boolean isFileValidated = false;
	
	private static Scanner scanner = new Scanner(System.in);
	private static boolean isLogging = false;
	private static Timer consoleTimer = new Timer("Timer Thread : Console");
	
	private static TimerTask logTask = new TimerTask(){public void run() {updateLog();}};
	private static TimerTask input = new TimerTask() {public void run() {executeCommand(scanner.nextLine());};};

	private static HashMap<String, WarpedAction> frameworkCommands = new HashMap<>();	
	private static HashMap<String, WarpedAction> consoleCommands = new HashMap<>();	
	
	static {
		consoleTimer.scheduleAtFixedRate(input, 0, UtilsMath.convertHzToMillis(1));
		frameworkCommands.put("/exit", () -> {  WarpedFramework2D.stop();});
		frameworkCommands.put("/inspect", () -> {  WarpedState.frameworkInspector.inspectTimers();});
		frameworkCommands.put("/stopLog", () -> { setLogging(false);});
		frameworkCommands.put("/startLog", () -> { setLogging(true);});
		frameworkCommands.put("/setRes720p", () -> {WarpedFramework2D.getWindow().setWindowResolution(1280, 720);});
		frameworkCommands.put("/setRes1080p", () -> {WarpedFramework2D.getWindow().setWindowResolution(1920, 1080);});
		frameworkCommands.put("/setRes4k", () -> {WarpedFramework2D.getWindow().setWindowResolution(3840, 2160);});
		frameworkCommands.put("/command", () -> {consoleCommands.keySet().forEach(k -> {WarpedState.notify.note(k);});});
		frameworkCommands.put("/help", () -> {frameworkCommands.keySet().forEach(k -> {WarpedState.notify.note(k);});});
	}
	

	
	/**Add a new command to the console.
	 * @param key - the string that will trigger the command when entered into the terminal or command line (GUI command line or eclipse console).
	 * @param command - the command to execute when the key is entered.
	 * @author 5som3*/
	public static void addCommand(String key, WarpedAction command) {consoleCommands.put(key, command);}
	
	/**Get a set of all the command keys.
	 * @return Set<String> - the keys.
	 * @author 5som3*/
	public static Set<String> getCommandKeys() {return frameworkCommands.keySet();}
	
	/**Remove a command from the console.
	 * @param key - the key of the command to remove.
	 * @author 5som3*/
	public static void removeCommand(String key) {
		if(key == null || key.equals("")) {
			Console.err("Console -> removeCommand() -> invalid command");
			return;
		}
		if(consoleCommands.containsKey(key)) {
			consoleCommands.remove(key);
			Console.ln("Console -> removeCommand() -> removed : " + key);
		} else Console.ln("Console -> removeCommand() -> no command exists with key : " + key);
	}
	
	/**Execute the command associated with the input key.
	 * @param key - the key of the command to execute.
	 * @author 5som3*/
	public static void executeCommand(String key) {
		Console.condition("Console -> executeCommand() -> attempting to execute : " + key);
		if(key == null || key.equals("")) {
			Console.err("Console -> executeCommand() -> invalid command");
			return;
		}
		if(frameworkCommands.containsKey(key)) frameworkCommands.get(key).action();
		else if(consoleCommands.containsKey(key)) {
			consoleCommands.get(key).action();
			Console.ln("Console -> executeCommand() -> executing command : " + key);
		} else {
			WarpedState.notify.note("enter '/help'");
			Console.err("Console -> executeCommand() -> console doesn't have command assigned to : " + key);
		}
	}

	/**Set if the console will be recorded to text file
	 * @param isLogging - if true all console lines will also be written to a text file in the log dirrectory.
	 * @author 5som3*/
	public static final void setLogging(boolean isLogging) {
		if(Console.isLogging != isLogging) {
			Console.isLogging = isLogging;
			if(Console.isLogging) {
				Console.ln("Console -> startLogging() -> started..");
				consoleTimer.schedule(logTask, 0, 1000);
			} else {
				Console.ln("Console -> stopLogging() -> stopped..");
				logTask.cancel();
			}
		} 
	}
	
	/**Is the console currently being logged.
	 * @return boolean - if true the console is being logged to text file else it is not logged.
	 * @author 5som3*/
	public static final boolean isLogging() {return isLogging;}
	
	/**Print a line of text to the console / terminal.
	 * @param text - the text will be printed in the console. (does not add a new line)
	 * @apiNote if logging the text will be logged in a text file.
	 * @author 5som3*/
	public static final void pr(String text) {
		System.out.print(text);
		if(isLogging) log(PRINT + text);
	}
	
	/**Print a line of text to the console / terminal.
	 * @param text - the text will be printed WHITE in the console. (adds a new line)
	 * @apiNote if logging the text will be logged in a text file.
	 * @author 5som3*/
	public static final void ln(String text) {
		System.out.println(text);
		if(isLogging) log(LINE + text);
	}
	
	/**Print a line of text to the console / terminal.
	 * @param text - the text will be printed YELLOW in the console. (adds a new line)
	 * @apiNote if logging the text will be logged in a text file.
	 * @author 5som3*/
	public static final void condition(String text) {
		ln(ConsoleColour.YELLOW, text);
		if(isLogging) log(CONDITION + text);
	};
	
	/**Print a line of text to the console / terminal.
	 * @param text - the text will be printed GREEN in the console. (adds a new line)
	 * @apiNote if logging the text will be logged in a text file.
	 * @author 5som3*/
	public static final void met(String text)  {
		ln(ConsoleColour.GREEN, text);
		if(isLogging) log(MET + text);
	};
	
	/**Print a line of text to the console / terminal.
	 * @param text - the text will be printed RED in the console. (adds a new line)
	 * @apiNote if logging the text will be logged in a text file.
	 * @author 5som3*/
	public static final void err(String text) {
		System.err.println(text);
		if(isLogging) log(ERROR + text);
	}

	/**Print a line of text to the console / terminal.
	 * @param text - the text will be printed BLUE in the console. (adds a new line)
	 * @apiNote if logging the text will be logged in a text file.
	 * @author 5som3*/
	public static final void blueln(String text)   {
		ln(ConsoleColour.CYAN, text);
		if(isLogging) log(BLUE + text);
	};
	
	/**Print a colour line of text to the console / terminal.
	 * @param consoleColour - the colour of the line of text.
	 * @param text - the text will be printed GREEN in the console. (adds a new line)
	 * @apiNote if logging the text will be logged in a text file.
	 * @author 5som3*/
	public static final void ln(ConsoleColour consoleColour, String text) {System.out.println(getAnsi(consoleColour) + text + ANSI_RESET);}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final void stackTrace(URISyntaxException exception) {
		ln(ConsoleColour.PURPLE, "URI SYNTAX EXCEPTION");
		if(isLogging) {
			log("URI SYNTAX EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final <T extends RuntimeException> void stackTrace(T exception)   {
		if(exception instanceof MediaException) mediaException((MediaException) exception);
		else if(exception instanceof IllegalArgumentException) illegalArgumentException((IllegalArgumentException) exception);
		else if(exception instanceof UnsupportedOperationException) unsupportedOperationException((UnsupportedOperationException)exception);
		else if(exception instanceof NullPointerException) nullPointerException((NullPointerException) exception);
		else {			
			ln(ConsoleColour.PURPLE, "? RUNTIME EXCEPTION");
			if(isLogging) {
				log("? RUNTIME EXCEPTION");
				logStackTrace(exception.getStackTrace());
			}
			exception.printStackTrace();
		}	
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final <K extends Exception> void stackTrace(K exception) {
		if(exception instanceof UnsupportedAudioFileException) unsupportedAudioFileException((UnsupportedAudioFileException) exception);
		else if(exception instanceof IOException) ioException((IOException) exception);
		else if(exception instanceof LineUnavailableException) lineUnavailableException((LineUnavailableException) exception);
		else {
			ln(ConsoleColour.PURPLE, "? RUNTIME EXCEPTION");
			if(isLogging) {
				log("? RUNTIME EXCEPTION");
				logStackTrace(exception.getStackTrace());
			}
			exception.printStackTrace();
		}	
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final void lineUnavailableException(LineUnavailableException exception) {
		ln(ConsoleColour.PURPLE, "LINE UNAVALIABLE EXCEPTION");
		if(isLogging) {
			log("LINE UNAVALIABLE EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final void ioException(IOException exception) {
		ln(ConsoleColour.PURPLE, "IO EXCEPTION");
		if(isLogging) {
			log("IO EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final void unsupportedAudioFileException(UnsupportedAudioFileException exception) {
		ln(ConsoleColour.PURPLE, "UNSUPPORTED AUDIO FILE EXCEPTION");
		if(isLogging) {
			log("UNSUPPORTED AUDIO FILE EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final void mediaException(MediaException exception) {
		ln(ConsoleColour.PURPLE, "MEDIA EXCEPTION");
		if(isLogging) {
			log("MEDIA EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final void illegalArgumentException(IllegalArgumentException exception) {
		ln(ConsoleColour.PURPLE, "ILLEGAL ARGUMENT EXCEPTION");
		if(isLogging) {
			log("ILLEGAL ARGUMENT EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}		
		exception.printStackTrace();
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final void unsupportedOperationException(UnsupportedOperationException exception) {
		ln(ConsoleColour.PURPLE, "UNSUPPORTED OPPERATION EXCEPTION");
		if(isLogging) {
			log("UNSUPPORTED OPPERATION EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	/**Print an exception to the console / terminal.
	 * @param exception - the exception to log.
	 * @apiNote if logging the text will be logged in a text file. 
	 * @author 5som3*/
	public static final void nullPointerException(NullPointerException exception) {
		ln(ConsoleColour.PURPLE, "NULL POINTER EXCEPTION");
		if(isLogging) {
			log("NULL POINTER EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	/**Stop the console timer.
	 * @implNote logging will not be able to be restarted after the console is stopped
	 * @author 5som3*/
	public static final void stop() {
		if(isLogging)updateLog();
		consoleTimer.cancel();
	}
	
	
	
	/**Writes any new console entrys to the log.
	 * @author 5som3 */
	private static final void updateLog() {		
		if(!validateDirectory()) return;
		if(!validateFile()) return;

			
		if(logFile == null || logDir == null) {
			Console.err("Console -> updateLog() -> something went wrong :(");
			return;
		}
		
		List<String> writeLog;
		index++;
		if(index > 1) {
			index = 0;
			writeLog = logB;
		} else  writeLog = logA;
		
		
		if(writeLog.size() == 0) {
			//Console.ln("Console -> updateLog() -> no log events to write");
			return;
		}

		try {
			for(int i = 0; i < writeLog.size(); i++) {				
				Files.write(Paths.get(logFilePath), (writeLog.get(i) + "\n").getBytes(), StandardOpenOption.APPEND);
			}
			writeLog.clear();
			//Console.met("Console -> updateLog() -> finished writing log");
			return;
		}catch (IOException e) {
		    //TODO implement exception handling
		}
		
		Console.err("Console -> updateLog() -> failed write log..");
	}	
	
	/**Validates the console directory
	 * @implNote will create the console log directory if it does not exist.
	 * @author 5som3*/
	private static final boolean validateDirectory() {
		if(isDirectoryValidated) return true;
		Console.condition("Console -> validateDirectory() -> directory hasn't been validated, checking dir..");
		logDir = new File(LOG_DIRECTORY);
		if(!logDir.exists()) {
			
			Console.condition("Console -> validateDirectory() -> log directory doesn't exist, creating dir..");
			if(logDir.mkdirs()) {
				Console.met("Console -> validateDirectory() -> log directory was created");	
				return isDirectoryValidated = true;
				
			} else {
				Console.err("Console -> validateDirectory() -> an error occured while creating the log directory, logging can not proceede");
				return isDirectoryValidated = false;
			}
			
		} else {			
			Console.met("Console -> validateDirectory() -> found directory");
			File[] contents = logDir.listFiles();
			if(contents.length > 0) {				
				Console.condition("Console -> validateDirectory() -> checking contents..");
				for(int i = 0; i < contents.length; i++) {
					File file = contents[i];
					String fileName = file.getName();
					Console.ln("Console -> checking file : " + file.getName());
					if(!fileName.startsWith("WarpedLog_")) {
						Console.condition("Console -> validateDirectory() -> file has incorect prefix, trying to delete invalid file..");
						if(file.delete()) Console.met("Console -> validateDirectory() -> removed invalid file : " + fileName);
						else {
							Console.err("Console -> validateDirectory() -> failed to delete file : " + fileName);
							return isDirectoryValidated = false;
						}
					} 
					if(!fileName.endsWith(SUFFIX)) {
						Console.condition("Console -> validateDirectory() -> file has incorect suffix, trying to delete invalid file..");
						if(file.delete()) Console.met("Console -> validateDirectory() -> removed invalid file : " + fileName);
						else {
							Console.err("Console -> validateDirectory() -> failed to delete file : " + fileName);
							logDir = null;
							return isDirectoryValidated = false;
						}
					}
				}
				Console.met("Console -> validateDirectory() -> directory is valid and contains " + contents.length + " log files");
				return isDirectoryValidated = true;
			} else {
				Console.met("Console -> validateDirectory() -> directory is valid, contains no files");
				return isDirectoryValidated = true; 
			}
			
		}
	}
	
	/**Validates the log file
	 * @implNote will create a log file to for the console to log entrys (if isLogging = true).
	 * @author 5som3*/
	private static final boolean validateFile() {
		if(isFileValidated) return true;
		Console.condition("Console -> validateFile() -> checking file..");
		LocalDate currentDate = LocalDate.now();
		String currentDateString = currentDate.toString();
		logFilePath = LOG_DIRECTORY + PREFIX + logDir.listFiles().length + "_" + currentDateString + "_" + SUFFIX;
		File file = new File(logFilePath);
		try {
			if(file.createNewFile()) {
				Console.met("Console -> validateFile() -> file was validated : " + logFilePath);
				logFile = file;
				return isFileValidated = true;
			} else {
				Console.err("Console -> validateFile() -> an error occured when creating the file : " + logFilePath );
				logFile = null;
				return isFileValidated = false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isFileValidated = false;	
	}
		
	
	private static final void log(String text) {
		if(index == 0) logA.add(text);
		else logB.add(text);	
	}

	private static final void logStackTrace(StackTraceElement[] stackTrace) {
		for(int i = 0; i < stackTrace.length; i++) {
			StackTraceElement e = stackTrace[i];
			String text = e.toString();
			ln(ConsoleColour.PURPLE, text);
			log(STACK + text);
		}
		
	}

	private static final String getAnsi(ConsoleColour ConsoleColour) {
		switch(ConsoleColour) {
		case BLACK:		return ANSI_BLACK;  
		case BLUE:		return ANSI_BLUE;   
		case CYAN:		return ANSI_CYAN;   
		case GREEN:		return ANSI_GREEN;  
		case PURPLE:	return ANSI_PURPLE; 
		case RED:		return ANSI_RED;    
		case WHITE:		return ANSI_WHITE; 
		case YELLOW:	return ANSI_YELLOW; 
				
		default:
			System.err.println("Console -> getAnsi() -> invalid case : " + ConsoleColour);
			return ANSI_BLACK;
		}
	}
	
	
}
