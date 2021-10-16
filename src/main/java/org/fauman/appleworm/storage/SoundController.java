package org.fauman.appleworm.storage;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.ugens.Sampler;

public final class SoundController {
	private static final SoundController instance = new SoundController();

	public static SoundController getInstance() {
		return instance;
	}
	
	// hardcoded settings _____________________________________:
	public static final int MAX_SAME_OVERLAP = 5;
	
	// hardcoded settings --------------------------------------:
	
	// general useful
	private static Minim minim;
	private static AudioPlayer background_music;
	private static HashMap<String, AudioPlayer> music;
	private static HashMap<String, Sampler> sounds;
	
	private SoundController() {
		minim = new Minim(Model.p_app);
		load_music();
		load_sounds();
		background_music = null;
		startBackgroundSong("The Sky of our Ancestors");
	}
	
	private void load_music() {
		music = new HashMap<>();
		URL url = getClass().getResource("/music/");
	    String path = url.getPath();
	    File folder = new File(path);
		String[] music_names = folder.list();
		for(int i = 0; i < music_names.length; ++i) {
			String file_name = music_names[i].replace(".mp3", "");
			music.put(file_name, minim.loadFile("music/" + file_name + ".mp3"));
		}
	}
	
	private void load_sounds() {
		sounds = new HashMap<>();
		AudioOutput out = minim.getLineOut();
		
		URL url = getClass().getResource("/sounds/");
	    String path = url.getPath();
	    File folder = new File(path);
		String[] music_names = folder.list();
		for(int i = 0; i < music_names.length; ++i) {
			String file_name = music_names[i].replace(".wav", "");
			Sampler sampler = new Sampler("sounds/" + file_name + ".wav", MAX_SAME_OVERLAP, minim);
			sampler.patch(out);
			sounds.put(file_name, sampler);
		}
	}
	
	public static void playSound(String sound_name) {
		sounds.get(sound_name).trigger();
	}
	
	public static void stopAllSounds() {
		for(Sampler sound : sounds.values()) {
			sound.stop();
		}
	}
	
	public static void startBackgroundSong(String music_name) {
		if(background_music != null) pauseBackgroundSong();
		background_music = music.get(music_name);
		restartBackgroundSong();
		resumeBackgroundSong();
	}
	
	public static void pauseBackgroundSong() {
		if(background_music != null) background_music.pause();
	}
	
	public static void restartBackgroundSong() {
		if(background_music != null) background_music.rewind();
	}
	
	public static void resumeBackgroundSong() {
		if(background_music != null) {
			int play_pos = background_music.position();
			background_music.loop();
			background_music.cue(play_pos);
		}
	}
}
