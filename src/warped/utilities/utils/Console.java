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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.MediaException;

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
	
	
	private static File logDir;
	private static File logFile;
	private static String logFilePath;
	
	private static List<String> logA = new ArrayList<>();
	private static List<String> logB = new ArrayList<>();
	
	private static int index = 0; 
	private static boolean isDirectoryValidated = false;
	private static boolean isFileValidated = false;

	public enum Colour {
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
	
	
	private static boolean isLogging = false;
	private static Timer logTimer = new Timer();
	private static TimerTask logTask = new TimerTask(){public void run() {updateLog();}};
		
	public static final void startLogging() {
		if(isLogging) {
			err("Console -> startLogging() -> console is already logging");
			return;
		}
		isLogging = true;
		Console.ln("Console -> startLogging() -> started..");
		logTimer.schedule(logTask, 0, 1000);
		
	}
	
	public static final void stopLogging() {
		if(!isLogging) {
			err("Console -> stopLogging() -> console is not logging");
			return;
		}
		Console.ln("Console -> stopLogging() -> stopped..");
		isLogging = false;
		logTimer.cancel();
	}
	
	
	
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
			Console.ln("Console -> updateLog() -> no log events to write");
			return;
		} else Console.condition("Console -> updateLog() -> trying to write " + writeLog.size() + " logs..");

		try {
			for(int i = 0; i < writeLog.size(); i++) {				
				Files.write(Paths.get(logFilePath), (writeLog.get(i) + "\n").getBytes(), StandardOpenOption.APPEND);
			}
			writeLog.clear();
			Console.met("Console -> updateLog() -> finished writing log");
			return;
		}catch (IOException e) {
		    //TODO implement exception handling
		}
		
		Console.err("Console -> updateLog() -> failed write log..");
	}	
	
	
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
			

	public static final void printStackTrace() {
	}
	
	public static final void ln(Colour colour, String text) {System.out.println(getAnsi(colour) + text + ANSI_RESET);}

	public static final void pr(String text) {
		System.out.print(text);
		if(isLogging) log(PRINT + text);
	}
	public static final void ln(String text) {
		System.out.println(text);
		if(isLogging) log(LINE + text);
	}
	public static final void err(String text) {
		System.err.println(text);
		if(isLogging) log(ERROR + text);
	}
	
	public static final void met(String text)  {
		ln(Colour.GREEN, text);
		if(isLogging) log(MET + text);
	};
	public static final void condition(String text) {
		ln(Colour.YELLOW, text);
		if(isLogging) log(CONDITION + text);
	};
	public static final void blueln(String text)   {
		ln(Colour.CYAN, text);
		if(isLogging) log(BLUE + text);
	};
	
	
	//--------
	//---------------- Exception Logging --------
	//--------Stack trace	
	public static final void stackTrace(URISyntaxException exception) {
		ln(Colour.PURPLE, "URI SYNTAX EXCEPTION");
		if(isLogging) {
			log("URI SYNTAX EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	public static final <T extends RuntimeException> void stackTrace(T exception)   {
		if(exception instanceof MediaException) mediaException((MediaException) exception);
		else if(exception instanceof IllegalArgumentException) illegalArgumentException((IllegalArgumentException) exception);
		else if(exception instanceof UnsupportedOperationException) unsupportedOperationException((UnsupportedOperationException)exception);
		else if(exception instanceof NullPointerException) nullPointerException((NullPointerException) exception);
		else {			
			ln(Colour.PURPLE, "? RUNTIME EXCEPTION");
			if(isLogging) {
				log("? RUNTIME EXCEPTION");
				logStackTrace(exception.getStackTrace());
			}
			exception.printStackTrace();
		}	
	}
	
	public static final <K extends Exception> void stackTrace(K exception) {
		if(exception instanceof UnsupportedAudioFileException) unsupportedAudioFileException((UnsupportedAudioFileException) exception);
		else if(exception instanceof IOException) ioException((IOException) exception);
		else if(exception instanceof LineUnavailableException) lineUnavailableException((LineUnavailableException) exception);
		else {
			ln(Colour.PURPLE, "? RUNTIME EXCEPTION");
			if(isLogging) {
				log("? RUNTIME EXCEPTION");
				logStackTrace(exception.getStackTrace());
			}
			exception.printStackTrace();
		}	
	}
	
	//---------Specific Exceptions
	public static final void lineUnavailableException(LineUnavailableException exception) {
		ln(Colour.PURPLE, "LINE UNAVALIABLE EXCEPTION");
		if(isLogging) {
			log("LINE UNAVALIABLE EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	public static final void ioException(IOException exception) {
		ln(Colour.PURPLE, "IO EXCEPTION");
		if(isLogging) {
			log("IO EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	public static final void unsupportedAudioFileException(UnsupportedAudioFileException exception) {
		ln(Colour.PURPLE, "UNSUPPORTED AUDIO FILE EXCEPTION");
		if(isLogging) {
			log("UNSUPPORTED AUDIO FILE EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	
	
	public static final void mediaException(MediaException exception) {
		ln(Colour.PURPLE, "MEDIA EXCEPTION");
		if(isLogging) {
			log("MEDIA EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	public static final void illegalArgumentException(IllegalArgumentException exception) {
		ln(Colour.PURPLE, "ILLEGAL ARGUMENT EXCEPTION");
		if(isLogging) {
			log("ILLEGAL ARGUMENT EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}		
		exception.printStackTrace();
	}
	
	public static final void unsupportedOperationException(UnsupportedOperationException exception) {
		ln(Colour.PURPLE, "UNSUPPORTED OPPERATION EXCEPTION");
		if(isLogging) {
			log("UNSUPPORTED OPPERATION EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	public static final void nullPointerException(NullPointerException exception) {
		ln(Colour.PURPLE, "NULL POINTER EXCEPTION");
		if(isLogging) {
			log("NULL POINTER EXCEPTION");
			logStackTrace(exception.getStackTrace());
		}
		exception.printStackTrace();
	}
	
	//---------------------------------------------------
	//---------------------------------------------------
	//---------------------------------------------------
	
	private static final void log(String text) {
		if(index == 0) logA.add(text);
		else logB.add(text);	
	}

	private static final void logStackTrace(StackTraceElement[] stackTrace) {
		for(int i = 0; i < stackTrace.length; i++) {
			StackTraceElement e = stackTrace[i];
			String text = e.toString();
			ln(Colour.PURPLE, text);
			log(STACK + text);
		}
		
	}

	private static final String getAnsi(Colour colour) {
		switch(colour) {
		case BLACK:		return ANSI_BLACK;  
		case BLUE:		return ANSI_BLUE;   
		case CYAN:		return ANSI_CYAN;   
		case GREEN:		return ANSI_GREEN;  
		case PURPLE:	return ANSI_PURPLE; 
		case RED:		return ANSI_RED;    
		case WHITE:		return ANSI_WHITE; 
		case YELLOW:	return ANSI_YELLOW; 
				
		default:
			System.err.println("Console -> getAnsi() -> invalid case : " + colour);
			return ANSI_BLACK;
		}
	}
	
	public static final void stop() {
		stopLogging();
		updateLog();
	}
	
}
