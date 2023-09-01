import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;

// переменные для картинок
    private Image dot;
    private Image apple;

    private int apple_x;
    private int apple_y;

    private int [] x = new int[ALL_DOTS];
    private int [] y = new int[ALL_DOTS];

    private int dots; // Размер-длинна змейки
    private Timer timer;

    private boolean left;
    private boolean right = true;
    private boolean up;
    private boolean down;
    private boolean InGame =true;


    public GameField()
    {
        setBackground(Color.black);
        loadImages();
        InitGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void InitGame()
    {
        dots = 3;
        // начальное положение змейки
        for(int i = 0; i < dots; i++)
        {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250,this);
        timer.start();
        createApple();
    }

    public void createApple ()
    {
        apple_x = new Random().nextInt(20) * DOT_SIZE;
        apple_y = new Random().nextInt(20) * DOT_SIZE;
    }

    // Метод для загрузки картинок
    public void loadImages()
    {
        ImageIcon iaa = new ImageIcon("apple.png");
        apple = iaa.getImage();
        ImageIcon idd = new ImageIcon("dot.png");
        dot = idd.getImage();
    }

    public void move()
    {
        // Изменение положения змейки
        for (int i = dots; i > 0 ; i--)
        {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (InGame)
        {
            g.drawImage(apple,apple_x,apple_y,this); // Рисуем яблоко на поле

            for (int i = 0; i < dots; i++)
            {
                g.drawImage(dot,x[i],y[i],this);
            }
        }
        else
        // Прописываем Game over
        {
            String str = "Game over";
            Font f = new Font("Arial", Font.BOLD,14);
            g.setColor(Color.white);
            g.setFont(f);
            g.drawString(str,125,SIZE / 2);
        }
    }

    // Нашли яблоко
public void checkApple()
{
    if (x[0] == apple_x && y[0] == apple_y)
    {
        // Увеличиваем змею
        dots ++;
        // Генерим снова яблоко
        createApple();
    }
}

// Метод проверки выхода за поле или столкновения самим с собой
public  void checkCollisions()
{
    for (int i = dots; i > 0 ; i--)
    {
        if (i > 4 && x[0] == x[i] && y[0] == y[i])
        {
            InGame = false;
        }
    }
    if (x[0] > SIZE) {
        InGame = false;
    }
    if (x[0] < 0 ) {
        InGame = false;
    }
    if (y[0] > SIZE) {
        InGame = false;
    }
    if (y[0] < 0) {
        InGame = false;
    }
}
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (InGame)
        {
            checkCollisions();
            move();
            checkApple();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;

            }
            if (key == KeyEvent.VK_UP && !down) {
                up = true;
                right = false;
                left = false;

            }
            if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                right = false;
                left = false;

            }
        }
    }
}
