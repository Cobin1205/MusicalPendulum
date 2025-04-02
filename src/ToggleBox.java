import processing.core.PApplet;
import processing.core.PConstants;

public class ToggleBox {
    public int x;
    public int y;
    public int width;
    public int height;
    public String label;
    public int[] color;
    private final PApplet prc;

    private int mx;
    private int my;
    public boolean toggled;
    public int value;

    private boolean mouseWasPressed;

    public ToggleBox(PApplet p, int x, int y, int width, int height, int val, String label) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.value = val;
        this.prc = p;

        this.color = new int[] {0, 100, 225};
        this.toggled = false;
        this.mouseWasPressed = false;
    }

    public void setColor(int r, int g, int b){
        color = new int[] {r, g, b};
    }

    public void update(){
        mx = prc.mouseX-400;
        my = prc.mouseY-200;
        if(!toggled){
            prc.fill(225, 225, 225); // Off color
        }
        else {
            prc.fill(color[0], color[1], color[2]);
            prc.rect(x, y+height, width, 5);
            //prc.fill(Math.max(0,color[0]-50), Math.max(0,color[1]-50), Math.max(0,color[2]-50)); //On color
        }

        //------------Display-----------------
        prc.rect(x, y, width, height);
        prc.fill(0);
        prc.textAlign(PConstants.CENTER, PConstants.CENTER);
        prc.text(label, x+(float)width/2, y-(float)height/2);
    }

    public boolean mouseOver(){
        return mx > x && mx < x + width && my > y && my < y + height;
    }

    public void click(){
        if (mouseOver() && prc.mousePressed && !mouseWasPressed) {
            toggled = !toggled;
        }
        if (prc.mousePressed) mouseWasPressed = true;
        else mouseWasPressed = false;
    }
}
