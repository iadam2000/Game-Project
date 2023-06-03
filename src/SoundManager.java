import java.nio.file.Paths;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource;

public class SoundManager {

	/**
	 * Sound id of the jump sound
	 */
	public static final int JUMP = 0;
	/**
	 * Sound id of the duck sound
	 */
	public static final int DUCK = 1;
	/**
	 * Sound id of the die sound
	 */
	public static final int DIE = 2;
	/**
	 * Sound id of the move layer sound
	 */
	public static final int MOVE_LAYER = 3;
	/**
	 * Sound id of level 1's music
	 */
	public static final int LEVEL_1 = 4;
	/**
	 * Sound id of level 2's music
	 */
	public static final int LEVEL_2 = 5;
	/**
	 * Sound id of level 3's music
	 */
	public static final int LEVEL_3 = 6;
	/**
	 * Sound id of level 4's music
	 */
	public static final int LEVEL_4 = 7;
	/**
	 * Sound id of level 5's music
	 */
	public static final int LEVEL_5 = 8;

	private SoundBuffer soundBuffers[];
	private Sound sounds[];
	private int numSounds, musicFrom, volume, musicVolume, maxMusicVol;
	private boolean muted;

	/**
	 * Constructor - sets up the volume and loads in all sounds.
	 */
	public SoundManager() {
		musicVolume = 50;
		volume = 50;
		maxMusicVol = 100;
		numSounds = 9;
		musicFrom = numSounds - 5;
		soundBuffers = new SoundBuffer[numSounds];
		sounds = new Sound[numSounds];
		muted = false;

		//init buffers
		for (int i = 0; i < numSounds; i++) {
			soundBuffers[i] = new SoundBuffer();
		}

		try {
			soundBuffers[0].loadFromFile(Paths.get("src/Assets/Sounds/Sfx/jump.wav"));
			soundBuffers[1].loadFromFile(Paths.get("src/Assets/Sounds/Sfx/ducking.wav"));
			soundBuffers[2].loadFromFile(Paths.get("src/Assets/Sounds/Sfx/crush-die.wav"));
			soundBuffers[3].loadFromFile(Paths.get("src/Assets/Sounds/Sfx/move-layers.wav"));

			soundBuffers[4].loadFromFile(Paths.get("src/Assets/Sounds/Music/Level 1-Modern day.wav"));
			soundBuffers[5].loadFromFile(Paths.get("src/Assets/Sounds/Music/Level 2-Industrial revolution.wav"));
			soundBuffers[6].loadFromFile(Paths.get("src/Assets/Sounds/Music/Level 3-Ancient Rome.wav"));
			soundBuffers[7].loadFromFile(Paths.get("src/Assets/Sounds/Music/Level 4-Dinosaurs.wav"));
			soundBuffers[8].loadFromFile(Paths.get("src/Assets/Sounds/Music/Level 5-Barren earth pre life.wav"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < numSounds; i++) {
			sounds[i] = new Sound();
			sounds[i].setBuffer(soundBuffers[i]);
			
			sounds[i].setVolume(volume);
			
			if(i >= musicFrom) {
				sounds[i].setVolume(musicVolume);
				sounds[i].setLoop(true);
			}
		}
	}
	
	/**
	 * Method to find out whether any sound is playing.
	 * @return true if any sound is playing, else false.
	 */
	public boolean anySoundPlaying() {
		for(Sound s : sounds) {
			if(s.getStatus() == SoundSource.Status.PLAYING) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to play a sound.
	 * @param id the sound ID to play - ids are given as statics of this class.
	 */
	public void playSound(int id) {
		if (!muted) {
			sounds[id].play();
		}
	}
	
	/**
	 * Method to play the music for a specific level
	 * @param lId level id of the level to play music for
	 */
	public void playMusicForLevel(int lId) {
		if(!muted) {
			if(lId <= 5) {
				sounds[musicFrom + lId - 1].play();
			}
		}
	}
	
	/**
	 * Method to pause the music for a specific level
	 * @param lId level id of the level to play music for
	 */
	public void pauseMusicForLevel(int lId) {
		if(lId <= 5) {
			sounds[musicFrom + lId - 1].pause();
		}
	}
	
	/**
	 * Method to stop all sounds from playing
	 */
	public void stopAllSounds() {
		for(int i = 0; i < sounds.length; i++) {
			sounds[i].stop();
		}
	}

	/**
	 * Method to mute/unmute sounds.
	 * If currently muted, calling will unmute and vice versa.
	 */
	public void toggleMuteSounds() {
		muted = !muted;
	}

	/**
	 * Method to find out whether sounds are muted.
	 * @return true if muted, else false
	 */
	public boolean isMuted() {
		return muted;
	}

	/**
	 * Method to increase the volume of the game's sounds
	 */
	public void volumeUp() {
		volume = (volume <= 90) ? volume + 10 : volume;
		musicVolume = (musicVolume <= maxMusicVol-10) ? musicVolume + 10 : musicVolume;
		
		for (int a = 0; a < numSounds; a++) {
			if(sounds[a].getVolume() <= 90) {
				if(a < musicFrom) {
					sounds[a].setVolume(volume);
				}
				else {
					if(sounds[a].getStatus() == SoundSource.Status.PLAYING) {
						sounds[a].pause();
						sounds[a].setVolume(musicVolume);
						sounds[a].play();
					}
					else {
						sounds[a].setVolume(musicVolume);
					}
				}
			}
		}
	}

	/**
	 * Method to decrease the volume of the game's sounds
	 */
	public void volumeDown() {
		volume = (volume >= 100 - maxMusicVol + 10) ? volume - 10 : volume;
		musicVolume = (musicVolume >= 10) ? musicVolume - 10 : musicVolume;
		
		
		
		for (int a = 0; a < numSounds; a++) {
			if(sounds[a].getVolume() >= 10) {
				if(a < musicFrom) {
					sounds[a].setVolume(volume);
				}
				else {
					if(sounds[a].getStatus() == SoundSource.Status.PLAYING) {
						sounds[a].pause();
						sounds[a].setVolume(musicVolume);
						sounds[a].play();
					}
					else {
						sounds[a].setVolume(musicVolume);
					}
				}
			}
		}
	}
}

