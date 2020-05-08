package com.dottycrumble;

import java.awt.Point;

public class Map
{
  public static final int NOT_EXPLORED = 0;
  public static final int DESTINATION = 1;
  public static final int OPENED = 2;
  public static final int SOURCE = -1;
  public static final int EXPLORED = -2;
  public static final int WALL = -3;
  public static final int PATH = -4;

  private int state[][];
  private int g[][];
  private double h[][];
  private Point parent[][];
  private int width;
  private int height;
  private int delay;
  
  public int getWidth() 
  {
    return width;
  }
  public int getHeight() 
  {
    return height;
  }
  public void addWall(int x, int y)
  {
    setState(x, y, Map.WALL);
  }
  public Map(int width, int height)
  {
    this.width = width;
    this.height = height;
    initState();
    initH();
    initG();
    initParent();
  }
  public void reset()
  {
    initState();
    initG();
    initH();
    initParent();
  }
  private void initState()
  {
    state = new int[height][width];
    for (int i = 0; i < height; i++)
      for (int j = 0; j < width; j++)
        state[i][j] = NOT_EXPLORED;
  }
  private void initG()
  {
    g = new int[height][width];
    for (int i = 0; i < height; i++)
      for (int j = 0; j < width; j++)
        g[i][j] = 0;
  }
  private void initH()
  {
    h = new double[height][width];
    for (int i = 0; i < height; i++)
      for (int j = 0; j < width; j++)
        h[i][j] = 0.0;
  }
  private void initParent()
  {
    parent = new Point[height][width];
    for (int i = 0; i < height; i++)
      for (int j = 0; j < width; j++)
        parent[i][j] = null;
  }
  public void setDelay(int delay)
  {
    this.delay = delay;
  }
  public void setState(int x, int y, int value)
  {
    state[y][x] = value;
  }
  public int getState(int x, int y)
  {
    if (x < 0 || x >= width || y < 0 || y >= height)
      return -1;
    else
      return state[y][x];
  }
  public void setG(int x, int y, int value)
  {
    g[y][x] = value;
  }
  public int getG(int x, int y)
  {
    if (x < 0 || x >= width || y < 0 || y >= height)
      return -1;
    else
      return g[y][x];
  }
  public void setH(int x, int y, double value)
  {
    h[y][x] = value;
  }
  public double getH(int x, int y)
  {
    if (x < 0 || x >= width || y < 0 || y >= height)
      return -1;
    else
      return h[y][x];
  }
  public void setParent(int x, int y, Point value)
  {
    parent[y][x] = value;
  }
  public Point getParent(int x, int y)
  {
    if (x < 0 || x >= width || y < 0 || y >= height)
      return null;
    else
      return parent[y][x];
  }
  public void clean()
  {
    initG();
    initH();
    initParent();
    for (int i = 0; i < height; i++)
    {
      for (int j = 0; j < width; j++)
      {
        if (state[i][j] != WALL)
          state[i][j] = NOT_EXPLORED;
      }
    }
  }
  private void clear()
  {
    for (int i = 0; i < 100; i++)
      System.out.println();
  }
  public void print()
  {
    clear();
    try
    {
      Thread.sleep(delay);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    for (int i = 0; i < height; i++)
    {
      for (int j = 0; j < width; j++)
      {
        switch (state[i][j])
        {
        case NOT_EXPLORED:
          System.out.print(". ");
          break;
        case PATH:
          System.out.print("* ");
          break;
        case WALL:
          System.out.print("# ");
          break;
        case OPENED:
          System.out.print("0 ");
          break;
        case SOURCE:
          System.out.print("X ");
          break;
        case DESTINATION:
          System.out.print("Y ");
          break;
        case EXPLORED:
          System.out.print("$ ");
          break;
        };
      }
      System.out.println();
    }
  }
}