package me.isaiah.shell.programs;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

/**
 * @author Aniruddha Dutta Chowdhury
 * @googleProfile http://www.google.com/profiles/a.d.chowdhury
 * @blog http://adchowdhury.blogspot.com/
*/
@ProgramInfo(name = "Minesweeper", version="1.0", authors="Aniruddha Dutta Chowdhury", width=300,height=400)
public class Minesweeper extends JProgram implements AWTEventListener, ActionListener {
    private static final long serialVersionUID = 1L;

    public static enum State { Clicked, Marked, Initial, WrongMarked }
    public static enum GameState { NotStarted, Playing, Finished }

    private static final int MAX_BOMB_COUNT  = 9;
    private int ROWS = 9, COLUMNS = 9, TOTAL = ROWS * COLUMNS;
    private JPanel pnlMain = new JPanel(new GridLayout(ROWS, COLUMNS));
    private JLabel lblBombCount = new JLabel(MAX_BOMB_COUNT + "");
    private JLabel lblTimer = new JLabel("0");
    private boolean isColorCheatOn = false;
    private JButton btnReset = new JButton("Reset");
    private Random r = new Random();
    private GameState state = GameState.NotStarted;

    private void startThread() {
        Thread th = new Thread(new Runnable() { public void run() {
            while (state == GameState.Playing) {
                lblTimer.setText((Long.parseLong(lblTimer.getText()) + 1) + "");
                lblTimer.updateUI();
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }});
        th.start();
    }

    public Minesweeper() {
        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        setContentPane(cp);
        cp.add(pnlMain, BorderLayout.CENTER);
        createButtons();
        addControlPanel();
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
    }

    private void restartGame() {
        state = GameState.NotStarted;
        lblTimer.setText("0");
        pnlMain.removeAll();
        createButtons();
        pnlMain.updateUI();
        lblBombCount.setText("" + MAX_BOMB_COUNT);
        lblBombCount.updateUI();
    }

    private void addControlPanel() {
        JPanel pnlTimer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTimer.add(lblTimer);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnReset.setToolTipText("<HTML>Press <B>F2</B> to reset the current game</HTML>");
        pnl.add(lblBombCount);
        pnl.add(btnReset);
        JPanel pnlN = new JPanel(new GridLayout(1, 3));

        pnlN.add(lblBombCount);
        pnlN.add(pnl);
        pnlN.add(pnlTimer);
        getContentPane().add(pnlN, BorderLayout.NORTH);
        btnReset.addActionListener(this);
    }

    private void createButtons() {
        List<Point> lstBombsLocation = new ArrayList<Point>();

        for (int row = 0; row < ROWS; row++) { for (int col = 0; col < COLUMNS; col++) {
            JButton btn = getButton(lstBombsLocation, TOTAL, new Point(row, col) {
                private static final long serialVersionUID = 1L;
                @Override public String toString() { return (int) getX() + ", " + (int) getY(); }
                @Override public boolean equals(Object obj) { return ((Point) obj).getX() == getX() && ((Point) obj).getY() == getY(); }
            });
            pnlMain.add(btn);
        }}
        while (lstBombsLocation.size() < MAX_BOMB_COUNT) updateBomds(lstBombsLocation, pnlMain.getComponents());
        for (Component c : pnlMain.getComponents()) updateBombCount((GameButton) c, pnlMain.getComponents());
    }

  private void updateBomds(List<Point> lstBombsLocation, Component[] components) {
    for (Component c : components) {
      Point location = ((GameButton) c).getPosition();
      if (r.nextInt(TOTAL) == new Double(((location.x) * COLUMNS) + location.getY()).intValue()) {
        ((GameButton) c).setBomb(true);
        lstBombsLocation.add(((GameButton) c).getPosition());
        return;
      }
    }
  }

  private GameButton getButton(List<Point> lstBombsLocation, int totalLocations, Point location) {
    GameButton btn = new GameButton(location);
    btn.setMargin(new Insets(0, 0, 0, 0));
    btn.setFocusable(false);
    if (lstBombsLocation.size() < MAX_BOMB_COUNT && isBomb()) {
        btn.setBomb(true);
        lstBombsLocation.add(location);
    }
    btn.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent mouseEvent) {
        if (state != GameState.Playing) {
          state = GameState.Playing;
          startThread();
        }
        GameButton button = (GameButton) mouseEvent.getSource();
        
        if (!(button.isEnabled())) return;

        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
          if (button.getState() == State.Marked) {
            button.setState(State.Initial);
            lblBombCount.setText((Long.parseLong(lblBombCount.getText()) + 1) + "");
            button.updateUI();
            return;
          }
          button.setState(State.Clicked);
          if (button.isBomb()) {
            blastBombs();
            return;
          } else if (button.getBombCount() == 0) 
              updateSurroundingZeros(button.getPosition());

          if (!checkGameState()) button.setEnabled(false);
        } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
          if (button.getState() == State.Marked) {
            button.setState(State.Initial);
            lblBombCount.setText((Long.parseLong(lblBombCount.getText()) + 1) + "");
          } else {
            button.setState(State.Marked);
            lblBombCount.setText((Long.parseLong(lblBombCount.getText()) - 1) + "");
          }
        }
        button.updateUI();
      }});
    return btn;
  }

  private boolean checkGameState() {
    boolean isWin = false;
    for (Component c : pnlMain.getComponents()) {
      GameButton b = (GameButton) c;
      if (b.getState() != State.Clicked) {
        if (b.isBomb()) isWin = true;
        else return false;
      }
    }
    if (isWin) {
      state = GameState.Finished;
      for (Component c : pnlMain.getComponents()) {
        GameButton b = (GameButton) c;
        if (b.isBomb()) b.setState(State.Marked);

        b.setEnabled(false);
      }
      JOptionPane.showMessageDialog(this, "You win the game :D", "Congrats", JOptionPane.INFORMATION_MESSAGE, null);
    }
    return isWin;
  }

  private void updateSurroundingZeros(Point currentPoint) {
    for (Point p : getSurroundings(currentPoint)) {
      GameButton b = getButtonAt(pnlMain.getComponents(), p);
      if (b != null && b.getBombCount() == 0 && b.getState() != State.Clicked && b.getState() != State.Marked && b.isBomb() == false) {
        b.setState(State.Clicked);
        updateSurroundingZeros(b.getPosition());
        b.updateUI();
      }
      if (b != null && b.getBombCount() > 0 && b.getState() != State.Clicked && b.getState() != State.Marked && b.isBomb() == false) {
        b.setEnabled(false);
        b.setState(State.Clicked);
        b.updateUI();
      }
    }
  }

  private void blastBombs() {
    int blastCount = 0;
    for (Component c : pnlMain.getComponents()) {
      ((GameButton) c).setEnabled(false);
      ((GameButton) c).transferFocus();
      if (((GameButton) c).isBomb() && ((GameButton) c).getState() != State.Marked) {
        ((GameButton) c).setState(State.Clicked);
        ((GameButton) c).updateUI();
        blastCount++;
      }
      if (!((GameButton) c).isBomb() && ((GameButton) c).getState() == State.Marked) ((GameButton) c).setState(State.WrongMarked);
    }
    lblBombCount.setText("" + blastCount);
    lblBombCount.updateUI();
    state = GameState.Finished;
    JOptionPane.showInternalMessageDialog(this, "You lost the game :(", "Game Over", JOptionPane.ERROR_MESSAGE, null);
    for (Component c : pnlMain.getComponents()) ((GameButton) c).setEnabled(false);
  }

  private boolean isBomb() {
    return r.nextInt(ROWS) == 1;
  }

  class GameButton extends JButton {
    private static final long serialVersionUID = 1L;
    private boolean isBomb = false;
    private Point position = null;
    private int bombCount = 0;
    private State state = State.Initial;

    public void setState(State state) {
      this.state = state;
      if (getBombCount() == 0 && !isBomb) setEnabled(false);
    }

    public State getState() {
      return state;
    }

    public int getBombCount() {
      return bombCount;
    }

    public void setBombCount(int bombCount) {
      this.bombCount = bombCount;
    }

    public GameButton(Point position) {
      setPosition(position);
      setText(position.toString());
    }

    public Point getPosition() {
      return position;
    }

    public void setPosition(Point position) {
      this.position = position;
    }

    public boolean isBomb() {
      return isBomb;
    }

    public void setBomb(boolean isBomb) {
      this.isBomb = isBomb;
    }

    @Override public String getText() {
      if (state == State.Initial) return "";
      if (state == State.Marked) return "\u00B6";
      if (state == State.Clicked) {
        if (isBomb) return "<html><font size='16'><b>*</b></font></html>";
        else { if (getBombCount() > 0) return getBombCount() + ""; else return ""; }
      }
      if (state == State.WrongMarked) return "X";

      return super.getText();
    }

    @Override public Color getBackground() {
      if (isColorCheatOn && isBomb) return Color.MAGENTA;

      if (state == State.Clicked) {
        if (isBomb) return Color.RED;
        if (getBombCount() > 0) return Color.GREEN;
      }
      if (isEnabled()) return Color.YELLOW.brighter(); else return super.getBackground();
    }
  }

  private Point[] getSurroundings(Point cPoint) {
    int cX = (int) cPoint.getX();
    int cY = (int) cPoint.getY();
    Point[] points = { new Point(cX - 1, cY - 1), new Point(cX - 1, cY), new Point(cX - 1, cY + 1), new Point(cX, cY - 1), new Point(cX, cY + 1), new Point(cX + 1, cY - 1), new Point(cX + 1, cY), new Point(cX + 1, cY + 1) };
    return points;
  }

  private void updateBombCount(GameButton btn, Component[] components) {
    for (Point p : getSurroundings(btn.getPosition())) {
      GameButton b = getButtonAt(components, p);
      if (b != null && b.isBomb()) btn.setBombCount(btn.getBombCount() + 1);
    }
    btn.setText(btn.getBombCount() + "");
  }

  private GameButton getButtonAt(Component[] components, Point position) {
    for (Component btn : components) if ((((GameButton) btn).getPosition().equals(position))) return (GameButton) btn;
    return null;
  }

  public void eventDispatched(AWTEvent event) {
    if (KeyEvent.class.isInstance(event) && ((KeyEvent) (event)).getID() == KeyEvent.KEY_RELEASED) {
     // if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F1 && isSelected())
      // TODO    Main.showNotification("Minesweeper by:\n Aniruddha Dutta Chowdhury\njShell ports by jShell", 5000, 220, 70);
      if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F2 && isSelected()) restartGame();
      if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F3 && isSelected()) {
        isColorCheatOn = !isColorCheatOn;
        if (state == GameState.Playing) pnlMain.updateUI();
      }

      if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F12) {
        for (Component c : pnlMain.getComponents()) {
          GameButton b = (GameButton) c;
          b.setState(b.isBomb() ? State.Marked : State.Clicked);
          b.setEnabled(false);
        }
        checkGameState();
      }
    }
  }

  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == btnReset) restartGame();
  }
}