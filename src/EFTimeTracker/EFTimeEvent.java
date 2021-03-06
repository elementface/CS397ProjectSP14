package EFTimeTracker;

import java.util.*;
import java.io.*;


//*EFTimeTracker Program*\\
//*PURPOSE:*\\
//*) Keep track of time events by tagging them with a name and fire rate per second (FPS)
//*) Keep track of the time (in ms) needed to satisfy one frame of the fireRatePS
//*) Keep track of a last triggered timer that will control when a boolean representing a trigger is flipped

//*HOW TO USE:*\\
//*1) Create an EFTimeTracker

//*GOALS:*\\
//*) make function to change eventname
//*) make function to change firerateps
public class EFTimeEvent 
{
  private String eventName;
  private double frameLength;
  private boolean isTriggered;
  private double timeSinceLastTrigger;
  private double minimumFrameLength;
  private double maximumFrameLength;
  
  private double[] runtimeSamples;
  private final int runtimeSampleLimit = 100;
  private int runtimeSampleCounter;
  private double initialFeedTime;
  private double finalFeedTime;
  
  private static double defaultFrameLength = 1000;
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public EFTimeEvent()
  {
    this( "", defaultFrameLength );
  }
  
  
  
  
  
  
  
  public EFTimeEvent( String eventName )
  {
    this( eventName, defaultFrameLength );
  }
  
  
  
  
  
  
  
  public EFTimeEvent( String eventName, double frameLength )
  {
    this.eventName = eventName;
    this.frameLength = frameLength;
    minimumFrameLength = 1;
    maximumFrameLength = Integer.MAX_VALUE;
    
    if( this.frameLength <= 0 ) this.frameLength = 1;
    
    isTriggered = false;
    timeSinceLastTrigger = 0L;
    
    runtimeSamples = new double[runtimeSampleLimit];
    
    clearRuntimeSamples();
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public void addTimeSinceLastTrigger( double addedTime )
  {
    
    if( addedTime < 0 ) return;
    timeSinceLastTrigger += addedTime;
    
    if( timeSinceLastTrigger >= frameLength )
    {
      isTriggered = true;
    }
  }
  
  
  
  
  
  
  
  public boolean getIsTriggered()
  {
    return isTriggered;
  }
  
  
  
  
  
  
  
  public boolean getAndFlipIsTriggered()
  {
    if( isTriggered )
    {
      int returnNumber = (int)(timeSinceLastTrigger % frameLength);
      timeSinceLastTrigger = returnNumber;
      isTriggered = false;
      return true;
    }
    
    return false;
  }
  
  
  
  
  
  
  
  public int getNumberOfTriggers()
  {
    int returnNumber = (int)(timeSinceLastTrigger / frameLength);
    return returnNumber;
  }
  
  
  
  
  
  
  
  public int getAndFlipNumberOfTriggers()
  {
    int returnNumber = (int)(timeSinceLastTrigger / frameLength);
    timeSinceLastTrigger = timeSinceLastTrigger % frameLength;
    isTriggered = false;
    return returnNumber;
  }
  
  
  
  
  
  
  
  public String getEventName()
  {
    return eventName;
  }
  
  
  
  
  
  
  
  public double getFrameLength()
  {
    return frameLength;
  }
  
  
  
  
  
  
  
  public double getTimeSinceLastTrigger()
  {
    return timeSinceLastTrigger;
  }
  
  
  
  
  
  
  
  public void setFrameLength( double frameLength )
  {
    
    this.frameLength = frameLength;
    
    if( this.frameLength <= minimumFrameLength ) this.frameLength = frameLength;
  }
  
  
  
  
  
  
  
  public void setEventName( String newName )
  {
    eventName = newName;
  }
  
  
  
  
  
  
  
  public void clearRuntimeSamples()
  {
    runtimeSampleCounter = 0;
    initialFeedTime = 0;
    finalFeedTime = 0;
    for( int i = 0; i < runtimeSampleLimit; i++ )
    {
      runtimeSamples[i] = Long.MAX_VALUE;
    }
  }
  
  
  
  
  
  
  
  public void feedInitialTime( double time )
  {
    initialFeedTime = time;
  }
  
  
  
  
  
  
  
  public void feedFinalTime( double time )
  {
    finalFeedTime = time;
    
    runtimeSamples[runtimeSampleCounter] = finalFeedTime - initialFeedTime;
    runtimeSampleCounter++;
    
    if( runtimeSampleCounter >= runtimeSampleLimit ) runtimeSampleCounter = 0;
  }
  
  
  
  
  
  
  
  public double getAverageRuntime()
  {
    
    double runtimeSum = 0;
    long sumCounter = 0;
    
    for( int i = 0; i < runtimeSampleLimit; i++ )
    {
      
      if( runtimeSamples[i] != Long.MAX_VALUE ) 
      {
        runtimeSum += runtimeSamples[i];
        sumCounter++;
      }
    }
    
    if( sumCounter != 0 ) return runtimeSum / sumCounter;
    return 0;
  }
  
  
  
  
  
  
  
  public double getMinimumFrameLength()
  {
    return minimumFrameLength;
  }
  
  
  
  
  
  
  
  public double getMaximumFrameLength()
  {
    return maximumFrameLength;
  }
  
  
  
  
  
  
  
  public double getLastRuntime()
  {
    int slot = runtimeSampleCounter - 1;
    if( slot < 0 ) slot = runtimeSampleLimit - 1;
    
    if( runtimeSamples[slot] == Long.MAX_VALUE ) return 0;
    return runtimeSamples[slot];
  }
  
  
  
  
  
  
  
  public void setDefaultFrameLength( long defaultFrameLength )
  {
    this.defaultFrameLength = defaultFrameLength;
  }
}