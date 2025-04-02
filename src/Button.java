import processing.core.PApplet;
import processing.core.PConstants;

public class Button {
    public int x;
    public int y;
    public int width;
    public int height;
    public String label;
    public int baseColor, currentColor, hoverColor;
    private final PApplet prc;

    private int mx;
    private int my;
    private boolean clicked;

    public Button(PApplet p, int x, int y, int width, int height, String label){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.prc = p;

        this.baseColor = 225;
        this.currentColor = baseColor;
        this.hoverColor = 200;
        this.clicked = false;
    }

    public void update(){
        mx = prc.mouseX-400;
        my = prc.mouseY-200;
        if(mouseOver()) {
            currentColor = hoverColor;
        }
        else currentColor = baseColor;

        //------------Display-----------------
        prc.fill(currentColor);
        prc.rect(x, y, width, height, 2);
        prc.fill(0);
        prc.textAlign(PConstants.CENTER, PConstants.CENTER);
        prc.textSize(13f);
        prc.text(label, x+(float)width/2, y+(float)height/2);
    }

    public boolean mouseOver(){
        return mx > x && mx < x + width && my > y && my < y + height;
    }

    public boolean isClicked(){
        if(mouseOver() && prc.mousePressed && !clicked){
            clicked = true;
            return true;
        }
        else if(!prc.mousePressed) {
            clicked = false;
        }
        return false;
    }
}
