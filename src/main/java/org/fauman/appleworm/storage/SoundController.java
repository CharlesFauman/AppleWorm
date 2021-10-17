package org.fauman.appleworm.storage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.ugens.Sampler;
import org.fauman.appleworm.util.FileHelper;

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
		ImmutableList<String> musics;
		try {
			musics = FileHelper.getChildren("/music");
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return;
		}
		for(String music_name : musics) {
			music.put(music_name, minim.loadFile("music/" + music_name + ".mp3"));
		}
	}
	
	private void load_sounds() {
		sounds = new HashMap<>();
		AudioOutput out = minim.getLineOut();

		ImmutableList<String> sound_names;
		try {
			sound_names = FileHelper.getChildren("/sounds");
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return;
		}
		for(String sound_name: sound_names) {
			Sampler sampler = new Sampler("sounds/" + sound_name + ".wav", MAX_SAME_OVERLAP, minim);
			sampler.patch(out);
			sounds.put(sound_name, sampler);
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
