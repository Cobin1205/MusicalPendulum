import processing.core.PApplet;
import processing.core.PConstants;

public class TextField {
    public int x, y, width, height;
    private final String label;
    public String text;
    public boolean active;
    private boolean typed, typeable;
    private PApplet prc;
    private int mx;
    private int my;
    public int value;
    private int iVal;
    private int lower, upper;

    public TextField(PApplet prc, int x, int y, int w, int h, String label, int iVal){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.label = label;
        this.text = String.valueOf(iVal);
        this.active = false;
        this.typed = true;
        this.typeable = true;
        this.prc = prc;
        this.value = iVal;
        this.iVal = iVal;
    }

    public int getVal(){ return value; };
    public void setBounds(int l, int u){
        this.lower = l;
        this.upper = u;
    }

    public void update(){
        mx = prc.mouseX-400;
        my = prc.mouseY-200;

        //--------- Activate or Deactivate ----------
        if(typeable) {
            if (mouseOver() && prc.mousePressed) {
                active = true;
            } else if (!mouseOver() && prc.mousePressed) {
                active = false;
            }
        }

        // -------- Accept input ---------
        if(active && !typed){
            // Type
            if(prc.key != PConstants.BACKSPACE && prc.key != PConstants.ENTER && '0' <= prc.key && prc.key <= '9'){
                text += prc.key;
                typed = true;
            }
            //Backspace
            else if(prc.key == PConstants.BACKSPACE && !text.isEmpty()){
                text = text.substring(0, text.length()-1);
                typed = true;
            }

            //Enter
            else if(prc.key == PConstants.ENTER){
                if(text=="")text=String.valueOf(iVal);
                text = String.valueOf(Math.min(Math.max(Integer.parseInt(text), lower), upper));
                value = Integer.parseInt(text);
                typed = true;
            }
        }

        //-------------Display---------------
        if(!active) prc.fill(225);
        if(active) prc.fill(200);
        prc.rect(x, y, width, height, 2);
        prc.fill(0);
        prc.textSize(13f);
        prc.textAlign(PConstants.LEFT, PConstants.CENTER);
        prc.text(text, x+5, y+(float)height/2);
        prc.text(label, x, y-(float)height/2);
    }

    public void resetTyped(){
        typed = false;
    }

    public void setTypeable(boolean p){
        typeable = p;
    }

    private boolean mouseOver(){
        return mx > x && mx < x + width && my > y && my < y + height;
    }
}
