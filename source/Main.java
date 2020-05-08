package com.dottycrumble;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JFrame;

public class Main extends JFrame implements MouseListener
{
  public static final int ROW = 10;
  public static final int COLUMN = 20;
  public static final int WIDTH = 960;
  public static final int HEIGHT = 600;
  public static final int INIT_STATE = 0;
  public static final int GAME_STATE = 1;
  public static final int GAME_OVER_STATE = 2;
  public static final int WHITE = 0;
  public static final int PINK = 1;
  public static final int PALE_BLUE = 2;
  public static final int LIGHT_ORANGE = 3;
  public static final int PALE_VIOLET = 4;
  public static final int PALE_LIME_GREEN = 5;
  public static final int PALE_GREEN = 6;
  public static final int BLACK = 7;

  private int state;
  private int clicked;
  private boolean running;
  private int fps = 60;
  private int frameCount = 0;
  private GamePanel gamePanel;
  private Map map;
  private Random random;
  private int dotColor[][];
  private Point hoverPoint;
  private Point sourcePoint;
  private Point destinationPoint;
  private ArrayList<Point> path;
  private ArrayList<Point> path2;
  private String clickStatus;
  private boolean pathFinish;
  private float loadingPercentage;
  private Font font;
  private int score;

  public void setState(int state)
  {
    this.state = state;
  }
  public int getState()
  {
    return state;
  }
  public int getColor(int x, int y)
  {
    return dotColor[y][x];
  }
  public void setColor(int x, int y, int value)
  {
    dotColor[y][x] = value;
  }
  public Point getHoverPoint()
  {
    return hoverPoint;
  }
  public String getClickStatus()
  {
    return clickStatus;
  }
  public Point getSourcePoint()
  {
    return sourcePoint;
  }
  public int getScore()
  {
    return score;
  }
  public static void main(String[] args)
  {
    Main main = new Main();
    main.run();
  }
  public Main()
  {
    gamePanel = new GamePanel(this);
    add(gamePanel);

    map = new Map(COLUMN, ROW);
    dotColor = new int[ROW][COLUMN];
    random = new Random();

    hoverPoint = new Point(-1, -1);

    setSize(WIDTH, HEIGHT);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setUndecorated(true);
    setResizable(false);
    setVisible(true);

    addMouseListener(this);
    running = false;

    initFont();
  }
  public void initFont()
  {
    try 
    {
     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")));
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }
  public void initColor()
  {
    for (int i = 0; i < Main.ROW; i++)
    {
      for (int j = 0; j < Main.COLUMN; j++)
      {
        dotColor[i][j] = random.nextInt(gamePanel.getColorSize() - 1) + 1;
        if (dotColor[i][j] >= BLACK)
        {
          map.addWall(j, i);
        }
      }
    }
  }
  public void run()
  {
    new Thread()
    {
       public void run()
       {
          running = true;
          gameLoop();
       }
    }.start();
  }
  private void gameLoop()
  {
    final double GAME_HERTZ = 30.0;
    final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
    final int MAX_UPDATES_BEFORE_RENDER = 5;
    double lastUpdateTime = System.nanoTime();
    double lastRenderTime = System.nanoTime();
    final double TARGET_FPS = 60;
    final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
    int lastSecondTime = (int) (lastUpdateTime / 1000000000);
    double now;
    int updateCount;
    float interpolation;
    int thisSecond;
    while (running)
    {

      now = System.nanoTime();
      updateCount = 0;
      while(now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER)
      {
        update();
        lastUpdateTime += TIME_BETWEEN_UPDATES;
        updateCount++;
      }
      if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
      {
        lastUpdateTime = now - TIME_BETWEEN_UPDATES;
      }
      interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
      render();
      lastRenderTime = now;
      thisSecond = (int) (lastUpdateTime / 1000000000);
      if (thisSecond > lastSecondTime)
      {
        fps = frameCount;
        frameCount = 0;
        lastSecondTime = thisSecond;
      }
      while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
      {
        Thread.yield();
        try 
        {
          Thread.sleep(1);
        } 
        catch(Exception e) 
        {
          e.printStackTrace();
        } 
        now = System.nanoTime();
      }

    }
  }
  public void update()
  {
    switch (state)
    {
      case GAME_STATE:
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        Point screenPosition = getLocation();
        Point mousePositionRelative = new Point(mousePosition.x - screenPosition.x, mousePosition.y - screenPosition.y);
        hoverPoint = new Point(-1, -1);
        if (mousePositionRelative.x > (WIDTH - COLUMN * gamePanel.getDotSpace()) / 2 && mousePositionRelative.x < WIDTH - (WIDTH - COLUMN * gamePanel.getDotSpace()) / 2)
        {
          if (mousePositionRelative.y > 50 && mousePositionRelative.y < ROW * gamePanel.getDotSpace() + 50)
          {
            int x = mousePositionRelative.x - (WIDTH - COLUMN * gamePanel.getDotSpace()) / 2;
            int y = mousePositionRelative.y - 50;
            hoverPoint.x = x / (gamePanel.getDotSpace());
            hoverPoint.y = y / (gamePanel.getDotSpace());
          }
        }

        if (path.size() > 0)
        {
          setColor(path.get(0).x, path.get(0).y, WHITE);
          path.remove(0);
        }
        else
          pathFinish = true;

        if (pathFinish)
        {
          if (path2.size() > 0)
          {
            int value = random.nextInt(gamePanel.getColorSize() - 1) + 1;
            setColor(path2.get(0).x, path2.get(0).y, value);
            if (dotColor[path2.get(0).y][path2.get(0).x] >= BLACK)
              map.addWall(path2.get(0).x, path2.get(0).y);
            path2.remove(0);
          }
          else
            pathFinish = false;
        }

        loadingPercentage -= 0.001;
        if (loadingPercentage <= 0 && path.size() == 0 && path2.size() == 0)
          setState(GAME_OVER_STATE);
        break;
    }
    gamePanel.update();
  }
  public void render()
  {
    gamePanel.render();
  }
  public float getLoadingPercentage()
  {
    return loadingPercentage;
  }
  public void startGame()
  {
    score = 0;
    map.reset();
    initColor();
    loadingPercentage = 1.0f;
    clicked = 0;
    pathFinish = false;
    path = new ArrayList<Point>();
    path2 = new ArrayList<Point>();
    setState(GAME_STATE);
    clickStatus = "Click one of the dot!";
  }
  public void clear()
  {
    for (int i = 0; i < 50; i++)
      System.out.println("");
  }
  public void printColor()
  {
    clear();
    for (int i = 0; i < ROW; i++)
    {
      for (int j = 0; j < COLUMN; j++)
      {
        if (getColor(j, i) < BLACK)
          System.out.printf("%d ", getColor(j, i));
        else
          System.out.printf(". ");
      }
      System.out.println();
    }
  }
  public void mouseClicked(MouseEvent event)
  {
    switch (state)
    {
      case INIT_STATE:

        Point playButtonPosition = gamePanel.getPlayButtonPosition();
        Point exitButtonPosition = gamePanel.getExitButtonPosition();
        int buttonWidth = gamePanel.getButtonWidth();
        int buttonHeight = gamePanel.getButtonHeight();
        if (event.getX() >= playButtonPosition.x && event.getX() <= playButtonPosition.x + buttonWidth)
          if (event.getY() >= playButtonPosition.y && event.getY() <= playButtonPosition.y + buttonHeight)
            startGame();
        if (event.getX() >= exitButtonPosition.x && event.getX() <= exitButtonPosition.x + buttonWidth)
          if (event.getY() >= exitButtonPosition.y && event.getY() <= exitButtonPosition.y + buttonHeight)
          {
            running = false;
            System.exit(0);
          }

        break;

      case GAME_STATE:
        if (event.getX() >=  75 && event.getX() <= 75 + gamePanel.getButtonWidth())
          if (event.getY() >= 525 &&  event.getY() <= 525 + gamePanel.getButtonHeight())
            setState(INIT_STATE);
          
        if (hoverPoint.x != -1 && hoverPoint.y != -1)
        {
          clicked++;
          clickStatus = "Click the second dot!";
          if (clicked == 1)
          {
            if (getColor(hoverPoint.x, hoverPoint.y) != BLACK)
              sourcePoint = new Point(hoverPoint);
            else
              clicked = 0;

          }
          else if (clicked == 2)
          {
            if (sourcePoint.x == hoverPoint.x &&  sourcePoint.y == hoverPoint.y)
            {

            }
            else if (getColor(sourcePoint.x, sourcePoint.y) == getColor(hoverPoint.x, hoverPoint.y))
            {
              destinationPoint = new Point(hoverPoint);
              path = new ArrayList<Point>();
              PathFind.aStar(map, sourcePoint, destinationPoint, path);
              Collections.reverse(path);
              score += path.size() * 10;
              path2 = new ArrayList<Point>(path);
              pathFinish = false;
            }
            clickStatus = "Click one of the dot!";
            sourcePoint = null;
            clicked = 0;
          }
        }

        break;

      case GAME_OVER_STATE:
        if (event.getX() >= (Main.WIDTH - gamePanel.getButtonWidth()) / 2 && event.getX() <= (Main.WIDTH - gamePanel.getButtonWidth()) / 2 + gamePanel.getButtonWidth())
          if (event.getY() >= 300 && event.getY() <= 300 + gamePanel.getButtonHeight())
            setState(INIT_STATE);
        break;
    }
    
  }
  public void mousePressed(MouseEvent event)
  {
  }
  public void mouseReleased(MouseEvent event)
  {
    
  }
  public void mouseEntered(MouseEvent event)
  {
  }
  public void mouseExited(MouseEvent event)
  {
    
  }
}