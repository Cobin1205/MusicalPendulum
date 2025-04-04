# Musical Pendulums
A brand new synthesizer that generates notes using double pendulums. This randomized approach of note selection results in a very interesting style.

## Features
1. Two modes for pendulum movement:
    1. Randomized rotation
    2. Real double pendulum physics

2. Capability to build chords by toggling notes
3. Adjustable octave range and detune
4. Adjustable bar length and joint mass

## Installation  
### 1. Install Java & Processing Core  
- Install **Java JDK 17+**: [AdoptOpenJDK](https://adoptopenjdk.net/)  
- Download **Processing Core Library**: [Processing Core JARs](https://github.com/processing/processing4/releases)  
- Download **JSyn Library**: [JSyn JAR](https://github.com/philburk/jsyn)  

### 2. Clone This Repository  
```sh
git clone https://github.com/Cobin1205/MusicalPendulum.git
cd MusicalPendulum.git
```

Open the project in intelliJ

Go to File -> Project Structure -> Libraries

Click Add -> Java and select the Jars core.jar (processing) and the lates jsyn jar file

Click Apply and OK

## Future Improvements
- Sound effects such as chorus, phaser, flanger, EQ, etc.
- Different events on the X axis
- Fix a bug on gravity mode: pendulums sometimes spin out of control
- Make design more visually appealing
- Turn this project into a .vst plugin?
