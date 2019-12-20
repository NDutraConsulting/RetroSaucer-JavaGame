package libs;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Audio clip class for playing sounds.
 * 
 * @author williamhooper
 */
public class AudioSample implements LineListener
{

    /**
     * Audio Sample states
     * 
     * @author williamhooper
     * 
     */
    public enum AudioSampleState
    {
        PLAYING, LOOPING, STOPPED, DONE, CLOSED
    };

    public static final int MAX_VOLUME = 11;
    public static final int LOOP_CONTINUOUSLY = Clip.LOOP_CONTINUOUSLY;

    private URL audioURL;
    private Clip audioClip;
    private AudioSampleState audioState;
    private AudioInputStream audioStream;

    /**
     * Constructor
     * 
     * @param obj
     * @param filename
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public AudioSample( Object obj, String filename ) throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        java.net.URL audioURL = obj.getClass().getResource( filename );
        if ( audioURL != null )
        {
            this.audioURL = audioURL;
            load();
            audioState = AudioSampleState.DONE;
        }
        else
        {
            throw new IOException( "Could not find file: " + filename );
        }
    }

    /**
     * Constructor
     * 
     * @param audioURL
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public AudioSample( URL audioURL ) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        this.audioURL = audioURL;
        load();
        audioState = AudioSampleState.DONE;
    }

    /**
     * Close the audio clip.
     * 
     */
    public void close()
    {
        if ( audioClip == null )
        {
            return;
        }

        synchronized ( audioClip )
        {
            audioClip.removeLineListener( this );
            audioClip.close();
            try
            {
                audioStream.close();
            }
            catch ( IOException e )
            {
                System.out.println( e.getMessage() );
            }

            /**
             * close the mixer (stops any running sounds)
             */
            Mixer mixer = AudioSystem.getMixer( null );
            if ( mixer.isOpen() )
            {
                mixer.close();
            }

            audioState = AudioSampleState.CLOSED;
        }
    }

    /**
     * Return the state of the audio clip
     * 
     * @return AudioSampleState
     */
    public AudioSampleState getState()
    {
        synchronized ( audioClip )
        {
            return audioState;
        }
    }

    /**
     * Get the length of the audio clip
     * 
     * @return long
     */
    public long getTime()
    {
        return audioClip.getMicrosecondLength();
    }

    /**
     * Loop the audio clip
     * 
     * @param loop
     */
    public void loop( int loop )
    {
        if ( audioClip == null )
        {
            return;
        }

        synchronized ( audioClip )
        {
            /**
             * rewind
             */
            audioClip.setFramePosition( 0 );

            /**
             * Start looping
             */
            audioClip.loop( loop );
            audioState = AudioSampleState.LOOPING;
        }
    }

    /**
     * Rewind the audio clip and play
     * 
     */
    public void play()
    {
        if ( audioClip == null )
        {
            return;
        }

        synchronized ( audioClip )
        {
            /**
             * rewind
             */
            audioClip.setFramePosition( 0 );

            /**
             * Start playing
             */
            audioClip.start();
            audioState = AudioSampleState.PLAYING;
        }
    }

    /**
     * Set the balance from -1.0 for left, 0.0 for center and 1.0 for right
     * 
     * @param range
     */
    public void setBalance( double range )
    {
        FloatControl gainControl = ( FloatControl ) audioClip.getControl( FloatControl.Type.BALANCE );
        gainControl.setValue( ( float ) range );
    }

    /**
     * Set the pan from -1.0 for left, 0.0 for center and 1.0 for right
     * 
     * @param range
     */
    public void setPan( double range )
    {
        FloatControl gainControl = ( FloatControl ) audioClip.getControl( FloatControl.Type.PAN );
        gainControl.setValue( ( float ) range );
    }

    /**
     * Set the volume from 0 (muted) to 11 (loud)
     * 
     * @param volume
     */
    public void setVolume( double volume )
    {
        FloatControl gainControl = ( FloatControl ) audioClip.getControl( FloatControl.Type.MASTER_GAIN );
        double gain = volume / 11.0;
        double dB = ( double ) ( Math.log( gain ) / Math.log( 10.0 ) * 20.0 );
        gainControl.setValue( ( float ) dB );
    }

    /**
     * Start playing the audio clip from the current position
     * 
     */
    public void start()
    {
        if ( audioClip == null )
        {
            return;
        }

        synchronized ( audioClip )
        {
            /**
             * Start playing
             */
            audioClip.start();
            audioState = AudioSampleState.PLAYING;
        }
    }

    /**
     * Stop playing the audio clip
     * 
     */
    public void stop()
    {
        if ( audioClip == null )
        {
            return;
        }

        synchronized ( audioClip )
        {
            /**
             * Stop playing
             */
            audioClip.stop();
            audioState = AudioSampleState.STOPPED;
        }
    }

    @Override
    public void update( LineEvent evt )
    {
        if ( evt.getType() == LineEvent.Type.STOP )
        {
            audioState = AudioSampleState.DONE;
        }
    }

    /**
     * Private method to load audio file
     * 
     * @throws LineUnavailableException
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    private void load() throws LineUnavailableException, UnsupportedAudioFileException, IOException
    {
        /**
         * From URL
         */
        audioStream = AudioSystem.getAudioInputStream( audioURL );

        /**
         * At present, ALAW and ULAW encodings must be converted to PCM_SIGNED before it can be played
         */
        AudioFormat audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info( SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED );

        if ( !AudioSystem.isLineSupported( info ) )
        {
            AudioFormat sourceFormat = audioFormat;
            AudioFormat targetFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16,
                    sourceFormat.getChannels(), sourceFormat.getChannels() * ( 16 / 8 ), sourceFormat.getSampleRate(), false );
            audioStream = AudioSystem.getAudioInputStream( targetFormat, audioStream );
            audioFormat = audioStream.getFormat();
        }

        /**
         * Create the clip
         */
        info = new DataLine.Info( Clip.class, audioFormat, ( ( int ) audioStream.getFrameLength() * audioFormat.getFrameSize() ) );
        audioClip = ( Clip ) AudioSystem.getLine( info );

        /**
         * Add a listener for line events
         */
        audioClip.addLineListener( this );

        /**
         * This method does not return until the audio file is completely loaded
         */
        audioClip.open( audioStream );
    }

}
