class TextEffect {
    String content;
    int size;
    float x, y;
    int life;

    TextEffect(String sometext, float xx, float yy) {
        content = sometext;
        size = 20;
        x = xx;
        y = yy;
        life = 50;
    }

    void decLife() {
        life--;
    }

    void decPos() {
        y--;
    }

    boolean dead() {
        return life <= 0;
    }

    void display() {
        textSize(size);
        textAlign(CENTER);
        fill(255 - 255 / 50 * life);
        text(content, x, y);
        decPos();
        decLife();
    }
}
