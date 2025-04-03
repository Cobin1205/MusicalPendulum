import processing.core.PApplet;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends PApplet {
    Stack<Pendulum> pendulums = new Stack<>();

    Synthesizer synth;
    LineOut lineOut;

    Button addButton;
    Button removeButton;
    Button clearButton;
    Button gravButton;
    Button oscButton;

    TextField octaveText;
    Button octaveAdd;
    Button octaveSub;

    TextField detuneText;
    Button detuneAdd;
    Button detuneSub;

    Button smoothButton;
    Button notesButton;

    TextField mass1Input;
    TextField mass2Input;
    TextField length1Input;
    TextField length2Input;
    Map<String, ToggleBox> nUI = new HashMap<>();

    int[][] noteColors = {
            {200, 30, 30}, {220, 60, 80},
            {240, 140, 50}, {250, 200, 60},
            {140, 210, 60}, {50, 170, 80},
            {40, 170, 140}, {50, 140, 220},
            {40, 70, 200}, {90, 50, 160},
            {160, 50, 200}, {210, 50, 140}};

    Map<Integer, int[]> stepToCol;

    public void settings(){
        size(600, 600);
    }

    public void setup(){
        background(255);

        //-------------------- Instantiate UI -----------------
        //Buttons
        addButton = new Button(this, -160, -190, 25, 25, "+");
        removeButton = new Button(this, -190, -190, 25, 25, "-");
        clearButton = new Button(this, 140, -190, 55, 25, "Clear");
        gravButton = new Button(this, -390, -190, 75, 25, "Gravity");
        oscButton = new Button(this, -310, -190, 75, 25, "Oscillate");
        smoothButton = new Button(this, 112, 330, 50, 25, "Smooth");
        notesButton = new Button(this, 112, 300, 50, 25, "Step");

        //Text Fields
        length1Input = new TextField(this, -390, -140, 75, 25, "Length 1", 100);
        length2Input = new TextField(this, -310, -140, 75, 25, "Length 2", 100);
        mass1Input = new TextField(this, -390, -90, 75, 25, "Mass 1", 10);
        mass2Input = new TextField(this, -310, -90, 75, 25, "Mass 2", 10);

        //Components of octave button
        octaveText = new TextField(this, 130, 220, 20, 20, "Octave", 2);
        octaveAdd = new Button(this, 152, 222, 15, 15, "+");
        octaveSub = new Button(this, 112, 222, 15, 15, "-");

        //Components of detune button
        detuneText = new TextField(this, 130, 260, 20, 20, "Detune", 2);
        detuneAdd = new Button(this, 152, 262, 15, 15, "+");
        detuneSub = new Button(this, 112, 262, 15, 15, "-");

        //Initialize values
        length1Input.setBounds(10, 100);
        length2Input.setBounds(10, 100);
        mass1Input.setBounds(1, 50);
        mass2Input.setBounds(1, 50);

        // JSyn Initialization
        synth = JSyn.createSynthesizer();
        lineOut = new LineOut();
        synth.add(lineOut);
        synth.start();
        lineOut.start();

        //Initialize the toggle notes, as well as their UI toggleButtons
        String[] nts = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};

        stepToCol = new HashMap<>();
        for(int i=0; i<12; i++){
            stepToCol.put(i, noteColors[i]);
        }

        for(int i=0; i<nts.length; i++){
            System.out.println(i);
            nUI.put(nts[i], new ToggleBox(this, -200+i*25,220, 20, 20, i, nts[i]));
            nUI.get(nts[i]).setColor(noteColors[i][0], noteColors[i][1], noteColors[i][2]);
        }
    }

    public void draw(){
        translate(400, 200);
        background(255);

        //Update steps
        Pendulum.steps.clear();
        for(ToggleBox box : nUI.values()){
            box.click();
            if(box.toggled){
                Pendulum.steps.add(box.value);
            }
        }

        //Update Pendulums
        for(Pendulum p : pendulums){
            p.update();
        }

        //----------- Draw The Graph -------------
        stroke(100, 100, 100);
        line(-200, -190, -200, 190); // Y axis
        textSize(15f); // Font Size
        fill(100, 100, 100); // Text color
        textAlign(LEFT, BOTTOM);

        //Draw the Dashes ----------------------- Somethings not working

        /*
        float dy = (float) 380f/(Pendulum.steps.size()*Pendulum.octaves);
        for(int i=0; i<Pendulum.steps.size()*Pendulum.octaves; i++){
            strokeWeight(2);
            int[] c = stepToCol.get(Pendulum.steps.get(i%Pendulum.steps.size()));
            stroke(c[0], c[1], c[2]);
            line(-202, -190 + dy*i, -198, -190 + dy*i);
            stroke(0);
            strokeWeight(1);
        }*/

        //------------- Update UI ---------------
        addButton.update();
        removeButton.update();
        clearButton.update();
        gravButton.update();
        oscButton.update();

        mass1Input.update();
        mass2Input.update();
        length1Input.update();
        length2Input.update();

        //Octave UI
        octaveText.update();
        octaveAdd.update();
        octaveSub.update();

        //Detune UI
        detuneText.update();
        detuneAdd.update();
        detuneSub.update();

        //Sound Type UI
        smoothButton.update();
        notesButton.update();

        //Note selection
        for(ToggleBox t : nUI.values()){
            t.update();
        }

        //------------- Button Events --------------
        //Add Pendulum to stack
        if(addButton.isClicked()){
            pendulums.add(new Pendulum(this, length1Input.getVal(), length2Input.getVal(), mass1Input.getVal(), mass2Input.getVal(), random(0, PI*2),random(0, PI*2), synth, lineOut));
        }
        //Remove Pendulum from stack
        if(removeButton.isClicked() && !pendulums.empty()){
            pendulums.getLast().triOsc.output.disconnect(0, lineOut.input, 0);
            pendulums.getLast().triOsc.output.disconnect(0, lineOut.input, 1);
            pendulums.pop();
        }
        //Clear stack of pendulums
        if(clearButton.isClicked()){
            for(Pendulum p : pendulums){
                p.triOsc.output.disconnect(0, lineOut.input, 0);
                p.triOsc.output.disconnect(0, lineOut.input, 1);
            }
            pendulums.clear();
        }

        //Activate Gravity
        if(gravButton.isClicked()) {
            if (Pendulum.oscMode.equals("oscillate")) {
                for (Pendulum p : pendulums) {
                    p.p1.vel = 0;
                    p.p1.acc = 0;
                    p.p2.vel = 0;
                    p.p2.acc = 0;
                }
                Pendulum.oscMode = "physics";
            }
        }
        //Deactivate gravity
        if(oscButton.isClicked()) Pendulum.oscMode = "oscillate";

        //Increment octave
        if(octaveAdd.isClicked()) octaveText.value++;
        //Decrement octave
        if(octaveSub.isClicked() && octaveText.value>1) octaveText.value--;
        Pendulum.octaves = octaveText.value;
        octaveText.text = String.valueOf(octaveText.value);

        //Increment detune
        if(detuneAdd.isClicked()) detuneText.value++;
        if(detuneSub.isClicked()) detuneText.value--;
        Pendulum.detuneMag = detuneText.value;
        detuneText.text = String.valueOf(detuneText.value);

        //Note Types UI
        if(smoothButton.isClicked()) Pendulum.noteMode = "smooth";
        if(notesButton.isClicked()) Pendulum.noteMode = "notes";
    }

    public void exit(){
        synth.stop();
        lineOut.stop();
        super.exit();
    }

    public static void main(String[] args){
        PApplet.main("Main");
    }

    public void keyReleased(){
        mass1Input.resetTyped();
        mass2Input.resetTyped();
        length1Input.resetTyped();
        length2Input.resetTyped();
    }
}