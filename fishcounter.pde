class FishCounter {
  int fish;
  float xpos, ypos;
  
  FishCounter() {
    fish = 0;
    ypos = height / 2;
    xpos = 30;
  }
  
  void display() {
    textAlign(CENTER);
    textSize(20);
    fill(0);
    text(fish, xpos, ypos);
  }
  
  void add() {
    fish++;
  }
  
  void reset() {
    fish = 0;
  }
}
