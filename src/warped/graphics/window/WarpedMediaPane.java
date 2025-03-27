/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import warped.WarpedFramework2D;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class WarpedMediaPane extends BorderPane {

	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	private final boolean repeat  = false;
	private boolean stopRequested = false;
	private boolean atEndOfMedia  = false;
	private Duration duration;
	private Slider timeSlider;
	private Label playTime;
	private Slider volumeSlider;
	private HBox mediaBar;
	private Media media;
	//private VectorD frameSize = new VectorD(1280, 540);
	private WarpedMediaPlayer parent;
	
	private Button playButton;

	private WarpedAction mediaEndAction;
	
	private boolean isSkippable = false;
	private boolean isPreparingMedia = true;
	private boolean isAutoPlay = false;
	private boolean isAutoFullscreen = false;
	
	private static Background playBackground;
	private static Background pauseBackground;
	private static Background fullscreenBackground;
	private static Background windowedBackground;
	private static Background jumpStartBackground;
	private static Background jumpEndBackground;
	private static Background stretchBackground;
	private static Background noStretchBackground;
	private static Background hideBackground;
	private static VectorD buttonSize = new VectorD(30.0, 30.0); 
	
	static {
		Image playImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(0), null);
		BackgroundImage playbImage  = new BackgroundImage(playImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
		playBackground = new Background(playbImage);                                                                                                                      
		                                                                                                                                                                  
		Image pImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(1), null);                                                                            
		BackgroundImage pbImage  = new BackgroundImage(pImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 		new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
		pauseBackground = new Background(pbImage);                                                                                                                       
		                                                                                                                                                                 
		Image fsImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(2), null);                                                                          
	    BackgroundImage fsbImage  = new BackgroundImage(fsImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 	new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
	    fullscreenBackground = new Background(fsbImage);                                                                                                                 
	                                                                                                                                                                     
	    Image wImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(3), null);                                                                           
		BackgroundImage wbImage  = new BackgroundImage(wImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,		new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
		windowedBackground = new Background(wbImage);                                                                                                                     
		                                                                                                                                                                  
		Image jsImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(4), null);                                                                           
		BackgroundImage jsbImage  = new BackgroundImage(jsImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,		new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
		jumpStartBackground = new Background(jsbImage);                                                                                                                  
		                                                                                                                                                                 
		Image jeImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(5), null);                                                                          
		BackgroundImage jebImage  = new BackgroundImage(jeImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 	new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
		jumpEndBackground = new Background(jebImage);                                                                                                                    
		                                                                                                                                                                 
		Image sImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(6), null);                                                                           
		BackgroundImage sbImage  = new BackgroundImage(sImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,		new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
		stretchBackground = new Background(sbImage);	                                                                                                                  
	                                                                                                                                                                      
		Image nsImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(7), null);                                                                           
		BackgroundImage nsbImage  = new BackgroundImage(nsImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,		new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
		noStretchBackground = new Background(nsbImage);                                                                                                                   
	    	                                                                                                                                                              
		Image hideImage = SwingFXUtils.toFXImage(FrameworkSprites.mediaIcons.getSprite(8), null);                                                                         
		BackgroundImage hidebImage  = new BackgroundImage(hideImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(buttonSize.x(), buttonSize.y(), true, true, true, false));
		hideBackground = new Background(hidebImage);	
	}
	
	private boolean isStretched  = false;
	private boolean isBarVisible = true;
	private boolean isFadeOut	 = false;
	private boolean isFadeIn 	 = false;
	
	private double opacity = 1.0;
	private int hideTick = 0;
	private int seekTick = 0;

	private static final double FADE = 0.03;
	private static final int HIDE_DELAY = 60;
	private static final int SEEK_DELAY = 20;
	
	public WarpedMediaPane(WarpedMediaPlayer parent) {
		this.parent = parent;
		initializePane();
		initializeUI();
	}

	public WarpedMediaPane(WarpedMediaPlayer parent, String path) {
		this.parent = parent;
		initializePane();
		initializeUI();
		loadFrameworkMedia(path);
	}
	
	private void initializePane() {
		Console.ln("WarpedMediaPane -> initializePane()");
		BackgroundFill backgroundFill = new BackgroundFill( Color. valueOf("#ff00ff"), new CornerRadii(10), new Insets(10) );
		Background background = new Background(backgroundFill);
		setBackground(background);
		setBorder(Border.EMPTY);
		
		setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
               Console.ln("WarpedMediaPane -> KeyReleased  :  " + e.getCode());
               if(!parent.isUserControlled()) return;
               if(e.getCode() == KeyCode.I) toggleBar();
               if(e.getCode() == KeyCode.F) parent.toggleFullscreen();
               if(e.getCode() == KeyCode.S) toggleStretched();
               if(e.getCode() == KeyCode.SPACE) {
            	   if(mediaPlayer == null) return;
            	   if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) mediaPlayer.pause();
            	   if(mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) mediaPlayer.play();
            	   if(mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) mediaPlayer.play();
               }
               
            }
            
            
        });
		
		setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
            	if(!parent.isUserControlled()) return;
            	hideTick = 0;
               	if(!isFadeOut) return;
               	else isFadeIn = true;
               
            }
        });
		
		setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
            	Console.ln("WarpedMediaPane -> onMouseRelase() -> mouse button : " + e.getButton());
            	if(isSkippable && isMediaPlayerReady()) {
            		Console.ln("WarpedMediaPane -> onMouseRelease() -> skipping media");
            		stop();
            		mediaEndAction.action();
            		return;
            	}
            	if(!parent.isUserControlled()) return;
            	if(isBarVisible) return;
            	if(mediaPlayer == null) return;
            	if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING && !atEndOfMedia) {            		
            		hideTick = 0;
            		isFadeIn = true;
            	} else {
            		opacity = 1.0;
            		mediaBar.setOpacity(opacity);
            		isBarVisible = true;
            		hideTick = 0;
            	}
            }
        });
		
		mediaView = new MediaView(null);
		mediaView.setStyle("-fx-background-color: #FFFFFF;");
		mediaView.fitWidthProperty().bind(widthProperty());
		mediaView.fitHeightProperty().bind(heightProperty());
		mediaView.setPreserveRatio(!isStretched);
		
		getChildren().add(mediaView);
	}

	private void initializeUI() {
		Console.ln("WarpedMediaPane -> inializeUI()");
		
	    mediaBar = new HBox();
	    mediaBar.setAlignment(Pos.CENTER);
	    mediaBar.setPadding(new Insets(5, 10, 5, 10));
	    
	    BorderPane.setAlignment(mediaBar, Pos.CENTER);

	    playButton = new Button();
	    playButton.setPrefSize(38.0, 38.0);
	    playButton.setBackground(playBackground);
	    playButton.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	        	if(mediaPlayer == null || !isBarVisible) return;
	            Status status = mediaPlayer.getStatus();
	            if (status == Status.UNKNOWN || status == Status.HALTED) return;
	            if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
	                if (atEndOfMedia) {
	                    mediaPlayer.seek(mediaPlayer.getStartTime());
	                    atEndOfMedia = false;
	                }
	                mediaPlayer.play();
	                playButton.setBackground(pauseBackground);
	            } else {
	                mediaPlayer.pause();
	                playButton.setBackground(playBackground);
	                hideTick = 0;
	                isFadeOut = false;
	                isFadeIn  = false;
	                opacity = 1.0;
	                mediaBar.setOpacity(opacity);
	                
	            }
	        }
	    });
	    
	    Button jumpStart = new Button();
	    jumpStart.setPrefSize(buttonSize.x(), buttonSize.y());
	    jumpStart.setBackground(jumpStartBackground);
	    jumpStart.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(mediaPlayer == null) return;
				mediaPlayer.seek(duration.multiply(0.0));
			}
	    	
	    });
	    
	    Button jumpEnd = new Button();
	    jumpEnd.setPrefSize(buttonSize.x(), buttonSize.y());
	    jumpEnd.setBackground(jumpEndBackground);
	    jumpEnd.setOnMouseReleased(new EventHandler<MouseEvent>() {
	 			@Override
	 			public void handle(MouseEvent event) {
	 				if(mediaPlayer == null) return;
	 				mediaPlayer.seek(duration.multiply(1.0));
	 			}
	 	    	
	 	});
	 	    
	    timeSlider = new Slider();
	    HBox.setHgrow(timeSlider, Priority.ALWAYS);
	    timeSlider.setMinWidth(50);
	    timeSlider.setMaxWidth(Double.MAX_VALUE);
	    timeSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {	
				if(mediaPlayer == null) return;
				mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
			}
	    	
	    });	
	    
	
	    timeSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {		
				if(mediaPlayer == null) return;
				hideTick = 0;
				seekTick++;
				if(event.isMiddleButtonDown()) seekTick+= 9;
				if(seekTick > SEEK_DELAY) {
					seekTick = 0;
					mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
				}
			}
	    });	 
	   

	    Button toggleFullscreen = new Button();
	    toggleFullscreen.setBackground(fullscreenBackground);  
	    toggleFullscreen.setPrefSize(buttonSize.x(), buttonSize.y());
	    toggleFullscreen.setOnMouseReleased(new EventHandler<MouseEvent>() {
	    	public void handle(MouseEvent e) {
	    		if(!isBarVisible) return;
	    		parent.toggleFullscreen();
	    		if(parent.isFullscreen()) toggleFullscreen.setBackground(windowedBackground);
	    		else toggleFullscreen.setBackground(fullscreenBackground);;
	    		
	    	}
	    });
	    
	    Button toggleStretch = new Button();
	    toggleStretch.setPrefSize(buttonSize.x(), buttonSize.y());
	    toggleStretch.setBackground(stretchBackground);
	    toggleStretch.setOnMouseReleased(new EventHandler<MouseEvent>() {
	    	public void handle(MouseEvent e) {
	    		if(!isBarVisible || mediaPlayer == null) return;
	    		toggleStretched();	    
	    		if(isStretched) toggleStretch.setBackground(noStretchBackground);
	    		else toggleStretch.setBackground(stretchBackground);
	    	}
	    });
	    
	    Button toggleBar = new Button();
	    toggleBar.setPrefSize(buttonSize.x(), buttonSize.y());
	    toggleBar.setBackground(hideBackground);
	    toggleBar.setOnMouseReleased(new EventHandler<MouseEvent>() {
	    	public void handle(MouseEvent e) {
	    		if(!isBarVisible) return;
	    		toggleBar();
	    	}
	    });
	    
	    

	    volumeSlider = new Slider(0,100,100);
	    volumeSlider.setMinWidth(30);
	    volumeSlider.setPrefWidth(70);
	    volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
	    volumeSlider.valueProperty().addListener(new InvalidationListener() {
	    	public void invalidated(Observable ov) {
	    		if(mediaPlayer == null || !isBarVisible) return;
	    		if (volumeSlider.isValueChanging()) {
	    			mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
	    		}
	    	}
	    });
	    
	    playTime = new Label();
	    playTime.setPrefWidth(90);
	    playTime.setMinWidth(50);
	    playTime.setTextFill(Color.LIGHTGRAY);
	    
	
	    Label spacer  = new Label("      ");
	    Label spacer6 = new Label("  ");
	    Label spacer7 = new Label("  ");
	    Label spacer1 = new Label("      ");
	    Label spacer2 = new Label("      ");
	    Label spacer4 = new Label("  ");
	    Label spacer5 = new Label("  ");
	    Label spacer8 = new Label("  ");
	    Label spacer9 = new Label("  ");
	   

	    mediaBar.getChildren().add(spacer8);
	    mediaBar.getChildren().add(jumpStart);
	    mediaBar.getChildren().add(spacer6);
	    mediaBar.getChildren().add(playButton);
	    mediaBar.getChildren().add(spacer7);
	    mediaBar.getChildren().add(jumpEnd);
	    mediaBar.getChildren().add(spacer);
	    mediaBar.getChildren().add(timeSlider);
	    mediaBar.getChildren().add(spacer1);
	    mediaBar.getChildren().add(playTime);
	    mediaBar.getChildren().add(toggleBar);
	    mediaBar.getChildren().add(spacer5);
	    mediaBar.getChildren().add(toggleStretch);
	    mediaBar.getChildren().add(spacer4);
	    mediaBar.getChildren().add(toggleFullscreen);
	    mediaBar.getChildren().add(spacer2);
	    mediaBar.getChildren().add(volumeSlider);
	    mediaBar.getChildren().add(spacer9);
	    
	    setBottom(mediaBar);
		if(!parent.isUserControlled()) {
			opacity = 0.0;
			mediaBar.setOpacity(opacity);
			isBarVisible = false;
		}
	}
	
	private String mediaPath;
	
	
	public void loadFrameworkMedia(String path) {
		this.mediaPath = path;
		//Loaded on framework class path, probably wont work for WarpedMediaFolder, need to use the link method established in warpedImageFolder
		Executors.newSingleThreadExecutor().execute(this::loadFrameworkMedia);    
	}
	
	public void loadFrameworkMedia() {
		isPreparingMedia = true;
		//FRAMEWORK CLASS PATH ONLY!
		URL url = WarpedFramework2D.class.getResource(mediaPath);
		if(url == null) {
			Console.err("WarpedMediaPane -> loadMedia() -> could not find media at path : " + mediaPath);
			return;
		} else Console.ln("WarpedMediaPane -> loadMedia() -> media exists, loading url path : " + mediaPath);
		
		
		URI uri = null;
		try {
			uri = url.toURI();
			Console.met("WarpedMediaPane -> loadMedia() -> url was converted to uri");
		} catch (URISyntaxException e) {
			Console.stackTrace(e);
		}
	
		if(uri == null) {
			Console.err("WarpedMediaPane -> loadMedia() -> url could not be converted to uri");
			return;
					
		}
		
		if(mediaPlayer != null) {
			Console.ln("WarpedMediaPane -> loadMedia() -> unloading old media");
			mediaPlayer.stop();
			mediaPlayer.dispose();
		}
				
		try {
			
			media = new Media(uri.toString());
			Console.ln("WarpedMediaPane -> loadMedia() -> created media..");
		} catch(MediaException  | IllegalArgumentException | UnsupportedOperationException | NullPointerException e) {
			Console.stackTrace(e);
		}
	   
	    if(media == null || media.getDuration().equals(Duration.ZERO)) {
	    	Console.err("WarpedMediaPane -> loadMedia() -> failed to load media");
	    	return;
	    } else Console.met("WarpedMediaPane -> loadMedia() -> media was loaded, setting media properties..");
	    
	    mediaPlayer = new MediaPlayer(media);  
	    mediaView.setMediaPlayer(mediaPlayer);
	    	    
	    mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
	        public void invalidated(Observable ov) {
	            updateValues();
	        }
	    });

	    mediaPlayer.setOnPlaying(new Runnable() {
	        public void run() {
	            if (stopRequested) {
	                mediaPlayer.pause();
	                stopRequested = false;
	            } 
	        }
	    });


	    mediaPlayer.setOnReady(new Runnable() {
	        public void run() {
	            duration = mediaPlayer.getMedia().getDuration();
	            updateValues();
	            Console.ln("WarpedMediaPane -> loadMedia() -> Media ready");
	            Console.ln("WarpedMediaPane -> loadMedia() -> loaded mead with duration: " + duration.toMinutes() + " min");
	            synchronized(this) {
	            	isPreparingMedia = false;
	            	notify();
	            	if(isAutoFullscreen) parent.setFullscreen(true);
	            	if(isAutoPlay) play();
	            }
	            
	        }
	    });

	    mediaPlayer.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
	    mediaPlayer.setOnEndOfMedia(new Runnable() {
	        public void run() {
	            Console.ln("Entre");
	            if (!repeat) {
	                stopRequested = true;
	                atEndOfMedia = true;
	                if(mediaEndAction != null) mediaEndAction.action();
	            }
	        }
	    });
	    
	    
	    if(mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
	    	Console.ln("WarpedMediaPane -> loadMedia() -> preparing media..");
	    	synchronized(this) {
	    		try {
	    			isPreparingMedia = true;
	    			wait();
	    		} catch (InterruptedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}	    		
	    	}
	    }
	    offsetMedia();
	}
	
	
	protected void toggleBar() {
		isFadeIn = false;
		isFadeOut = false;
		hideTick = 0;
		if(isBarVisible) {
			isBarVisible = false;
			opacity = 0.0;
		} else {
			isBarVisible = true; 
			opacity = 1.0;
		}
		mediaBar.setOpacity(opacity);
	}
		
	private void toggleStretched() {
		if(isStretched) isStretched = false;
		else isStretched = true;
		
		mediaView.setPreserveRatio(!isStretched);
		offsetMedia();
	}
	
	public void setEndOfMediaAction(WarpedAction action) {mediaEndAction = action;}
	
	//protected void setFrameSize(int x, int y) {setFrameSize((double)x, (double)y);}
	
	/*
	protected void setFrameSize(double x, double y) {
		Console.ln("WarpedMediaPane -> setFrameSize() -> frame size set to : " + frameSize.getString());
		frameSize.set(x, y);
		if(media != null) offsetMedia();
	}
	*/
	
	
	public void offsetMedia() {
		if(media == null || mediaPlayer == null) {
			Console.err("WarpedMediaPane -> offsetMedia() -> media is null");
			return;
		} else if(!isMediaPlayerReady()){			
			Console.err("WarpedMediaPane -> offestMedia -> mediaPlayer is in invalid state : " + mediaPlayer.getStatus());
			return;
		}
		
		double mediaAspectRatio = (double)media.getWidth() / (double)media.getHeight();
		Console.ln("WarpedMediaPane -> offsetMedia() -> media aspect ratio : " + mediaAspectRatio);
		
		int xOffset = 0;
		int yOffset = 0;
		
		if(!isStretched) {			
			if(parent.getFrameWidth() >= parent.getFrameHeight()) {
				if(parent.getFrameWidth() > media.getWidth()) {				
					xOffset = (int)((parent.getFrameWidth() - parent.getFrameHeight() * mediaAspectRatio) / 2.0);
				} else xOffset = 0;
			} else {
				if(parent.getFrameHeight() > media.getHeight()) {				
					yOffset = (int)(parent.getFrameHeight() / 2) - (int)((parent.getFrameWidth() / mediaAspectRatio) / 2);
				} else yOffset = 0;
			}
		}

		Console.ln("WarpedMediaPane -> offsetMedia() -> media offset : ( " + xOffset + ", " + yOffset + ")");

		mediaView.setLayoutX(xOffset);
		mediaView.setLayoutY(yOffset);
	}
	
	protected void updateValues() {
	    if (mediaPlayer == null || playTime == null || timeSlider == null || volumeSlider == null) {
	    	Console.err("WarpedMediaPlayer -> updateValues() -> media pane has one or more null componenets");
	    	return;
	    }
	    
	    Platform.runLater(new Runnable() {
	    	public void run() {
	    		if(mediaPlayer == null) return;
	    		duration = mediaPlayer.getMedia().getDuration();
	    		Duration currentTime = mediaPlayer.getCurrentTime();
	    		playTime.setText(formatTime(currentTime, duration));
	    		timeSlider.setDisable(duration.isUnknown());
	    		if (!timeSlider.isDisabled()
	    				&& duration.greaterThan(Duration.ZERO)
	    				&& !timeSlider.isValueChanging()) {
	    			timeSlider.setValue(currentTime.divide(duration).toMillis()
	    					* 100.0);
	    		}
	    		if (!volumeSlider.isValueChanging()) {
	    			volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume()
	    					* 100));
	    		}
	    		
	    		
	    		if(isBarVisible) {
	    			hideTick++;
	    			if(hideTick > HIDE_DELAY) {
	    				hideTick = 0;
	    				isFadeOut = true;
	    			}
	    		}
	    		
	    		if(isFadeOut) {
	    			if(isFadeIn) {
	    				isFadeOut = false;
	    				hideTick = 0;
	    			}
	    			opacity -= FADE;
	    			if(opacity < 0.0) {
	    				opacity = 0.0;
	    				isFadeOut = false;
	    				isBarVisible = false;
	    			}
	    			mediaBar.setOpacity(opacity);
	    		}
	    		
	    		if(isFadeIn) {          	
	    			opacity += FADE;
	    			if(opacity > 1.0) {
	    				opacity = 1.0;
	    				isFadeIn = false;
	    				isBarVisible = true;
	    			}
	    			mediaBar.setOpacity(opacity);
	    		}
	    		
	    		
	    	}
	    });
	}

	private static String formatTime(Duration elapsed, Duration duration) {
		String result;
		int intElapsed = (int) Math.floor(elapsed.toSeconds());
	    int elapsedHours = intElapsed / (60 * 60);
	    if (elapsedHours > 0) {
	        intElapsed -= elapsedHours * 60 * 60;
	    }
	    int elapsedMinutes = intElapsed / 60;
	    int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
	            - elapsedMinutes * 60;

	    if (duration.greaterThan(Duration.ZERO)) {
	        int intDuration = (int) Math.floor(duration.toSeconds());
	        int durationHours = intDuration / (60 * 60);
	        if (durationHours > 0) {
	            intDuration -= durationHours * 60 * 60;
	        }
	        int durationMinutes = intDuration / 60;
	        int durationSeconds = intDuration - durationHours * 60 * 60
	                - durationMinutes * 60;
	        if (durationHours > 0) {
	            result = String.format("%d:%02d:%02d/%d:%02d:%02d",
	                    elapsedHours, elapsedMinutes, elapsedSeconds, durationHours, durationMinutes, durationSeconds);
	        } else {
	            result = String.format("%02d:%02d/%02d:%02d",
	                    elapsedMinutes, elapsedSeconds, durationMinutes, durationSeconds);
	        }
	    } else {
	        if (elapsedHours > 0) {
	            result = String.format("%d:%02d:%02d", elapsedHours,
	                    elapsedMinutes, elapsedSeconds);
	        } else {
	            result = String.format("%02d:%02d", elapsedMinutes,
	                    elapsedSeconds);
	        }
	    }
	    
	    while(result.endsWith(" ")) {
	    	Console.ln("WarpedMediaPane -> formatTime() -> removed exra characters");
	    	result = result.substring(0, result.length() - 1);
	    }
	    return result;
	}
	
	public boolean isMediaPlayerReady() {
		if(mediaPlayer == null									  ||
		   mediaPlayer.getStatus() == MediaPlayer.Status.UNKNOWN  ||
		   mediaPlayer.getStatus() == MediaPlayer.Status.DISPOSED || 
		   mediaPlayer.getStatus() == MediaPlayer.Status.HALTED   ||
		   mediaPlayer.getStatus() == MediaPlayer.Status.STALLED  ||
		   isPreparingMedia) return false;
		else return true;
	}
	
	public void play() {
		if(mediaPlayer != null) {
			if(isMediaPlayerReady()) {
				Console.ln("WarpedMediaPane -> play()");
				mediaPlayer.play();
				playButton.setBackground(pauseBackground);
			} else Console.err("WarpedMediaPlayer -> play() -> mediaPlayer is not ready");
		} else Console.err("WarpedMediaPlayer -> play() -> no media is loaded");
	}
	
	public void stop() {
		if(mediaPlayer != null) {
			if(isMediaPlayerReady()) {	
				Console.ln("WarpedMediaPane -> stop()");
				mediaPlayer.stop();
				playButton.setBackground(playBackground);
			} else Console.err("WarpedMediaPlayer -> stop() -> mediaPlayer is not ready");
		} else Console.err("WarpedMediaPlayer -> stop() -> no media is loaded");
	}
	
	public void pause() {
		if(mediaPlayer != null) {
			if(isMediaPlayerReady()) {				
				Console.ln("WarpedMediaPane -> pause()");
				mediaPlayer.pause();
				playButton.setBackground(playBackground);
			} else Console.err("WarpedMediaPlayer -> pause() -> mediaPlayer is not ready");
		} else Console.err("WarpedMediaPlayer -> pause() -> no media is loaded");
	}
	
	public void unload() {
		if(mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.dispose();
			mediaPlayer = null;
		} else Console.err("WarpedMediaPlayer -> unload() -> no media is loaded");
	}
	
	public void setVolume(double volume) {
		if(mediaPlayer != null) {
			if(isMediaPlayerReady()) {
				if(volume < 0.0) {Console.err("WarpedMediaPlayer -> setVolume() -> volume must be greater than 0.0"); volume = 0.0;}
				else if(volume > 1.0) {Console.err("WarpedMediaPlayer -> setVolume() -> volume must be greater than 1.0"); volume = 1.0;}
				else mediaPlayer.setVolume(volume);
			} else Console.err("WarpedMediaPlayer -> setVolume() -> mediaPlayer is not ready");
		} else Console.err("WarpedMediaPlayer -> setVolume() -> no media is loaded");
	}
	
	public void seek(double time) {
		if(mediaPlayer != null) {
			if(isMediaPlayerReady()) {
				if(time < 0.0) {Console.err("WarpedMediaPlayer -> seek() -> time must be greater than 0.0"); time = 0.0;}
				else if(time > 1.0) {Console.err("WarpedMediaPlayer -> seek() -> time must be less than 1.0"); time = 1.0;}
				else mediaPlayer.seek(duration.multiply(time));
			} else Console.err("WarpedMediaPlayer -> seek() -> mediaPlayer is not ready");
		} else Console.err("WarpedMediaPlayer -> seek() -> mo media is loaded");
	}
		
	public void setSkippable(boolean isSkippable) {
		Console.ln("WarpedMediaPane -> setSkippable() -> set to : " + isSkippable);
		this.isSkippable = isSkippable;
	}
	public void setAutoPlay(boolean isAutoPlay) {
		Console.ln("WarpedMediaPane -> setAutoPlay() -> set to : " + isAutoPlay);
		this.isAutoPlay = isAutoPlay;
	}
	public void setAutoFullscreen(boolean isAutoFullscreen) {
		Console.ln("WarpedMediaPane -> setAutoFullscreen() -> set to : + " + isAutoFullscreen);
		this.isAutoFullscreen = isAutoFullscreen;
	}
	
}
