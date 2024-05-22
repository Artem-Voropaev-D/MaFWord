import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

//монотонность - Свойство - ... : Если одна поверхность находится внутри другой, то интеграл по внешней поверхности обычно больше, чем интеграл по внутренней поверхности.
//        инвариантность - Свойство - ... относительно ориентации: Поверхностные интегралы могут быть ... относительно ориентации поверхности, что означает, что они могут быть одинаковыми для двух поверхностей, которые являются обратно ориентированными.
//        аддитивность - Свойство - Поверхностные интегралы обычно обладают свойством ... .
//        зависимость - ... от параметризации: Поверхностные интегралы могут ... от параметризации поверхности.
//        перестановка - Свойство - ... интеграла и предела:
//        сдвиг - Свойство - ... во времени: Если вы задерживаете функцию f(x) на некоторое постоянное время t

public class MaFWord_kaif {

    static double full_hd_width = 1536;

    static double full_hd_height = 864;

    private static JFrame jFrame = getFrame();
    private static String Value = "gggg";
    private static Clip clip;
    private static boolean a = true;
    private static boolean b = true;
    private static boolean c = true;
    static MyComponent myComponent = new MyComponent();

    public static void StartMenu(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        double coef_w = dimension.width / full_hd_width;

        double coef_h = dimension.height / full_hd_height;

        jFrame.add(myComponent);

        Font fontg = new Font("Arial Black", Font.BOLD, dimension.width*dimension.height/150000);
        JButton OProg = new JButton("О программе");
        OProg.setFont(fontg);

        OProg.setBounds( (int)(20 * coef_w), (int)(20 * coef_h), dimension.width*35/384, dimension.height*15/432); //КРОССПЛАТФОРМЕННОСТЬ
        jFrame.add(OProg);
        OProg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame pobeda = new JFrame();
                pobeda.setBounds((int)(20 * coef_w), (int)(50 * coef_h), dimension.width/6, dimension.height/6);
                pobeda.setTitle("MaFWord");
                Image image = new ImageIcon("img/MathWord.png").getImage();
                pobeda.setIconImage(image);
                pobeda.setVisible(true);
                JLabel label1 = new JLabel("<html>Авторы:<br>Воропаев Артём Дмитриевич<br>Шкурко Никита Владимирович<br>Версия: 1.0<br>2023 год</html>");
                label1.setBounds(0, 0, 30, 30);
                pobeda.add(label1);
                pobeda.setLayout(null);
                label1.setBounds((int)(20 * coef_w), (int)(5 * coef_h), (int)(400 * coef_w), (int)(100 * coef_h));
            }
        });
        Font font = new Font("Courier New", Font.BOLD, dimension.height*dimension.width/87000);

        JButton Prepod = new JButton("Инструкция");
        Prepod.setFont(font);
        //РАЗМЕРЫ КНОПКИ КРОССПЛАТФОРМЕННОСТЬ

        Prepod.setBounds(dimension.width/2 - dimension.width*28/512, dimension.height/6 - dimension.height*5/864, dimension.width*28/256, dimension.height*463/10000);
        jFrame.add(Prepod);
        Prepod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame_prepod = new JFrame();
                frame_prepod.setBounds((int)(20 * coef_w), (int)(50 * coef_h), dimension.width/3, dimension.height/2);
                frame_prepod.setTitle("MaFWord");
                Image image = new ImageIcon("img/MathWord.png").getImage();
                frame_prepod.setIconImage(image);
                frame_prepod.setVisible(true);
                JLabel label1 = new JLabel("<html>Инструкция для преподавателей!<br>Для того, чтобы добавить " +
                        "необходимые термины и их<br>определения в список кроссворда, необходимо:<br>" +
                        "1.В исходных файлах программы в папке 'img' открыть текстовый документ Base;<br>" +
                        "2.Заполнить данный документ желаемыми терминами и их определениями, придерживаясь следующим образцам:<br>" +
                        "1.аддитивность - Свойство - Поверхностные интегралы обычно обладают свойством ... .<br>" +
                        "2.зависимость - ... от параметризации: Поверхностные интегралы могут ... от параметризации поверхности.<br>" +
                        "То есть необходимо следовать следующим правилам заполнения:<br>" +
                        "- Термин, который будет добавлен в список кроссворда пишется первым и с маленькой буквы<br>" +
                        "- Определение, которое будет показано пользователю пишется после пробела, тире и пробела, т.е. 'термин - определение'<br>" +
                        "- Слово, которое нужно отгадать пользователю заменяется многоточием <br></html>");
                label1.setBounds(0, 0, 30, 30);
                frame_prepod.add(label1);
                frame_prepod.setLayout(null);
                label1.setBounds((int)(20 * coef_w), (int)(5 * coef_h), (int)(400 * coef_w), (int)(350 * coef_h));
            }
        });


        JButton NewGame = new JButton("Начать игру");
        NewGame.setFont(font);
        NewGame.setBounds(dimension.width/2 - dimension.width*28/512, dimension.height/8 - dimension.height*5/216, dimension.width*28/256, dimension.height*463/10000);
        jFrame.add(NewGame);
        NewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                b = false;
                jFrame.getContentPane().removeAll();
                jFrame.repaint();

                String [][] mass = new String[30][30];
                ArrayList<StringBuilder> words = new ArrayList<>();
                Map<String, String> map_op = new HashMap<>();



                File file = new File("img/Base.txt");
                try {
                    FileReader reader = new FileReader(file);
                    BufferedReader b_reader = new BufferedReader(reader);
                    String line_in_base = b_reader.readLine();
                    String word_in_base;
                    String def_in_base;
                    if(line_in_base.equals("")){
                        JFrame Zapolnenie = new JFrame();
                        Zapolnenie.setBounds((int)(20 * coef_w), (int)(50 * coef_h), dimension.width/5, dimension.height/8);
                        Zapolnenie.setTitle("MaFWord");
                        Image image = new ImageIcon("img/MathWord.png").getImage();
                        Zapolnenie.setIconImage(image);
                        Zapolnenie.setVisible(true);
                        JLabel label_Zapolnenie = new JLabel("<html>Для создания кроссворда необходима<br> минимум одна пара: термин - определение!</html>");
                        label_Zapolnenie .setBounds(0, 0, 30, 30);
                        Zapolnenie.add(label_Zapolnenie );
                        Zapolnenie.setLayout(null);
                        label_Zapolnenie .setBounds((int)(20 * coef_w), (int)(5 * coef_h), (int)(300 * coef_w), (int)(50 * coef_h));
                        b = true;
                        c = true;
                        jFrame.getContentPane().removeAll();
                        jFrame.repaint();
                        StartMenu();
                    }

                    while (line_in_base != null){
                        int i = 0;
                        while(line_in_base.charAt(i) != ' '){
                            i++;
                        }
                        word_in_base = line_in_base.substring(0, i);
                        def_in_base = "<html>" +  line_in_base.substring(i + 3) + "</html>";
                        words.add(new StringBuilder(word_in_base));
                        map_op.put(word_in_base, def_in_base);

                        line_in_base = b_reader.readLine();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }



//                words.add(new StringBuilder("монотонность"));
//                words.add(new StringBuilder("инвариантность"));
//                words.add(new StringBuilder("аддитивность"));
//                words.add(new StringBuilder("зависимость"));
//                words.add(new StringBuilder("перестановка"));
//                words.add(new StringBuilder("сдвиг"));
//                words.add(new StringBuilder("ограниченность"));
//                words.add(new StringBuilder("свертка"));
//                words.add(new StringBuilder("инверсия"));
//                words.add(new StringBuilder("разбиение"));
//                words.add(new StringBuilder("симметрия"));
//                words.add(new StringBuilder("порядок"));
//                words.add(new StringBuilder("линейность"));
//                words.add(new StringBuilder("координаты"));
//                words.add(new StringBuilder("уравнения"));
//                words.add(new StringBuilder("поверхности"));
//                words.add(new StringBuilder("кривые"));
//                words.add(new StringBuilder("параметр"));
//                words.add(new StringBuilder("идентитет"));
//                words.add(new StringBuilder("сходимость"));
//                words.add(new StringBuilder("двойной"));
//                words.add(new StringBuilder("полярные"));
//                words.add(new StringBuilder("сумма"));
//                words.add(new StringBuilder("замена"));
//                words.add(new StringBuilder("объем"));
//                words.add(new StringBuilder("фубини"));
//                words.add(new StringBuilder("тройной"));
//                words.add(new StringBuilder("поверхностный"));
//                words.add(new StringBuilder("параметризация"));
//                words.add(new StringBuilder("криволинейные"));
//                words.add(new StringBuilder("поле"));
//                words.add(new StringBuilder("дифференцирование"));
//                words.add(new StringBuilder("интегрирование"));
//                words.add(new StringBuilder("предел"));
//                words.add(new StringBuilder("непрерывность"));
//                words.add(new StringBuilder("фурье"));
//                words.add(new StringBuilder("коэффициенты"));
//                words.add(new StringBuilder("интеграл"));
//                words.add(new StringBuilder("спектр"));
//                words.add(new StringBuilder("преобразование"));
//                words.add(new StringBuilder("функция"));
//                words.add(new StringBuilder("анализ"));
//
//
//                map_op.put("монотонность", "<html>Свойство - ... : Если одна поверхность находится внутри другой, то интеграл по внешней поверхности обычно больше, чем интеграл по внутренней поверхности.</html>");
//                map_op.put("инвариантность", "<html>Свойство - ... относительно ориентации: Поверхностные интегралы могут быть ... относительно ориентации поверхности, что означает, что они могут быть одинаковыми для двух поверхностей, которые являются обратно ориентированными.</html>");
//                map_op.put("аддитивность", "<html>Свойство - Поверхностные интегралы обычно обладают свойством ... . Это означает, что интеграл по объединению двух непересекающихся поверхностей равен сумме интегралов по каждой из этих поверхностей.</html>");
//                map_op.put("зависимость", "<html>Свойство - ... от параметризации: Поверхностные интегралы могут ... от параметризации поверхности. Это означает, что выбор параметризации может повлиять на конечное значение интеграла.</html>");
//                map_op.put("перестановка", "<html>Свойство - ... интеграла и предела: Важным свойством интегралов, зависящих от параметра, является возможность менять порядок интегрирования и взятия предела, что может привести к различным математическим результатам.</html>");
//                map_op.put("сдвиг", "<html>Свойство - ... во времени: Если вы задерживаете функцию f(x) на некоторое постоянное время t, то ее интеграл Фурье будет умножен на комплексную экспоненту, зависящую от t.</html>");
//                map_op.put("ограниченность", "<html>Свойство - ... : Интеграл Фурье ...для функций, у которых интеграл по модулю бесконечен (например, ...по амплитуде).</html>");
//                map_op.put("свертка", "<html>Свойство - ... : ... двух функций во времени соответствует умножению их интегралов Фурье в частотной области.</html>");
//                map_op.put("инверсия", "<html>Свойство - ... : Интеграл Фурье обратим, то есть исходную функцию можно восстановить из ее интеграла Фурье, и наоборот, при помощи обратного интеграла Фурье.</html>");
//                map_op.put("разбиение", "<html>Свойство - ... области: Двойной интеграл по области D можно вычислить, ... D на более мелкие подобласти и интегрировать по каждой из них. Это особенно полезно, когда D имеет сложную форму.</html>");
//                map_op.put("симметрия", "<html>Свойство - ... : Если функция f(x, y) является четной (f(-x, -y) = f(x, y)) или нечетной (f(-x, -y) = -f(x, y)) относительно одной или обеих переменных, это может упростить вычисление двойных интегралов.</html>");
//                map_op.put("порядок", "<html>Свойство - Изменение ... (ка) интегрирования: ... интегрирования можно изменять без изменения результата. То есть, если функция f(x, y) непрерывна на некоторой области D, то: ∬D f(x, y) dA = ∬D f(x, y) dy dx = ∬D f(x, y) dx dy</html>");
//                map_op.put("линейность", "<html>Свойство - ... : Двойные интегралы ..., что означает, что интеграл от суммы двух функций равен сумме интегралов каждой из функций: ∬[f(x, y) + g(x, y)] dA = ∬f(x, y) dA + ∬g(x, y) dA</html>");
//                map_op.put("координаты", "<html>Параметрические криволинейные ... : Это система ... , в которой положение точки задается параметрами, а интегралы, зависящие от параметра, могут использоваться для анализа физических явлений в таких координатах.</html>");
//                map_op.put("уравнения", "<html>Параметрически заданные ... : Это ... , которые связывают параметры и независимые переменные в математическом описании кривых, поверхностей и других объектов. Интегралы, зависящие от параметра, часто используются для решения систем таких ... .</html>");
//                map_op.put("поверхности", "<html>Параметрически заданные ... : Аналогично кривым, параметрически заданные ... задаются с использованием параметров. Интегралы, зависящие от параметра, могут быть использованы для вычисления площади ... , объема тела или других характеристик таких ... .</html>");
//                map_op.put("кривые", "<html>Параметрически заданные ... : Это ... в плоскости или пространстве, которые задаются с использованием параметров. Интегралы, зависящие от параметра, могут быть использованы для вычисления длины дуги, площади под кривой и других характеристик таких кривых.</html>");
//                map_op.put("параметр", "<html>Интеграл, зависящий от ...(а) : Это интеграл, в котором интегрирование выполняется по переменной, которая сама зависит от параметра. Такой интеграл записывается как ∫f(x, a) dx, где \"a\" - ... , изменяющийся в определенных пределах, а \"x\" - независимая переменная.</html>");
//                map_op.put("идентитет", "<html>Парсеваля-... : Важное тождество в теории рядов и интегралов Фурье, связывающее интегралы функции и интегралы её разложения Фурье.</html>");
//                map_op.put("сходимость", "<html>... ряда Фурье: Изучение того, насколько хорошо ряд Фурье приближает заданную функцию, а также условий, при которых сходимость достигается.</html>");
//                map_op.put("двойной" , "<html>... интеграл - это математический инструмент, используемый для вычисления площадей,объемов и других характеристик областей в двумерных и трехмерных пространствах. Он обобщает понятие обычного интеграла на случай, когда функция зависит от двух независимых переменных.</html>");
//                map_op.put("полярные" , "<html>Иногда для упрощения вычислений используются ... координаты, когда область D задается в радиально-угловых координатах. В этом случае, двойной интеграл может быть выражен в новых переменных - радиусе и угле.</html>");
//                map_op.put("сумма" , "<html>Иногда для упрощения вычислений используются ... координаты, когда область D задается в радиально-угловых координатах. В этом случае, двойной интеграл может быть выражен в новых переменных - радиусе и угле.</html>");
//                map_op.put("замена" , "<html>Для упрощения вычислений можно использовать ... переменных в двойных интегралах. ... переменных позволяет перейти к новым координатам, где интеграл может быть более легко вычислен. Например, ... переменных может упростить интеграл в прямоугольных координатах, приводя его к интегралу в полярных координатах.</html>");
//                map_op.put("объем" , "<html>Двойной интеграл можно интерпретировать как ... тела, ограниченного поверхностью z = f(x, y) и плоской областью D на плоскости xy. Этот ... может быть положительным или отрицательным, в зависимости от значения функции f(x, y) и выбора области D.</html>");
//                map_op.put("фубини" , "<html>Теорема ... утверждает, что двойной интеграл можно вычислять как последовательные интегралы по одной переменной. Это позволяет разбивать сложные интегралы на более простые компоненты.</html>");
//                map_op.put("тройной" , "<html>... интеграл представляет собой расширение понятия определенного интеграла на трехмерное пространство и позволяет вычислять объемы и средние значения функций в трехмерных областях. Обозначается как ∭, где интегрирование выполняется по трехмерной области в пространстве.</html>");
//                map_op.put("поверхностный" , "<html>... интеграл первого рода определяет интеграл функции, заданной на поверхности, и представляет собой интеграл этой функции по элементам площади на поверхности. Он может быть использован для вычисления различных физических величин, таких как масса, центр массы и момент инерции.</html>");
//                map_op.put("параметризация" , "<html>... поверхности - это процесс представления поверхности в виде функции от двух параметров (обычно u и v), который позволяет выразить координаты точек на поверхности в зависимости от этих параметров. ... облегчает вычисление поверхностных интегралов.</html>");
//                map_op.put("криволинейные" , "<html>Поверхностные интегралы могут быть вычислены через ... координаты, где поверхность может быть параметризована более сложными функциями, что позволяет работать с более сложными геометрическими объектами.</html>");
//                map_op.put("поле" , "<html>... - это математическая концепция, представляющая собой функцию, которая определяет значение в каждой точке пространства (или пространства-времени). В физике ... описывают физические величины, такие как электрическое ... , магнитное ... и другие.</html>");
//                map_op.put("дифференцирование" , "<html>... - это процесс нахождения производной функции, который описывает её скорость изменения в каждой точке.</html>");
//                map_op.put("интегрирование" , "<html>... - это процесс нахождения интеграла функции, который позволяет вычислить площадь под кривой функции или найти обратную операцию дифференцированию.</html>");
//                map_op.put("предел" , "<html>... - это концепция, используемая в математическом анализе для определения значения, к которому стремится функция при приближении аргумента к определённой точке.</html>");
//                map_op.put("непрерывность" , "<html>... - это свойство функции, при котором её значения изменяются плавно и без разрывов в заданном диапазоне аргументов.</html>");
//                map_op.put("фурье" , "<html>Ряд ... - это представление функции в виде бесконечной суммы синусов и косинусов (или комплексных экспонент) с различными частотами. Он используется для аппроксимации функций и анализа периодических или кусочно-периодических сигналов.</html>");
//                map_op.put("коэффициенты" , "<html>... Фурье: Это числа, которые определяют веса синусов и косинусов (или комплексных экспонент) в разложении Фурье функции. ... Фурье можно вычислить с помощью интегралов или суммирования, в зависимости от контекста.</html>");
//                map_op.put("интеграл" , "<html>... Фурье - это метод, используемый для преобразования функции из области времени в область частоты. Он представляет собой интеграл от функции, умноженной на комплексный экспонент с различными частотами. Этот ... позволяет анализировать спектральное содержание сигнала.</html>");
//                map_op.put("спектр" , "<html>... функции - это набор коэффициентов Фурье, которые описывают, из каких синусов и косинусов (или комплексных экспонент) состоит разложение Фурье этой функции. ... позволяет определить, какие частоты присутствуют в сигнале и с какой амплитудой.</html>");
//                map_op.put("преобразование" , "<html>... Фурье - это операция, которая переводит функцию из области времени в область частоты. Оно является инструментом для анализа спектральных характеристик сигналов и часто используется в обработке сигналов, теории информации и других областях.</html>");
//                map_op.put("функция" , "<html>Периодическая ...: ..., которая имеет периодически повторяющиеся значения. Разложение Фурье может быть использовано для анализа и аппроксимации таких ... .</html>");
//                map_op.put("анализ" , "<html>... Гиббса: Явление, при котором разложение Фурье дает аппроксимацию функции, которая на границах периода имеет \"отсеченные\" пики, известные как явление Гиббса.</html>");

                for (int i = 0; i < 30; i++){
                    for( int j = 0; j<30; j++){
                        mass[i][j] = ".";
                    }
                }

                Random rand = new Random();
                int w = rand.nextInt(words.size());

                Map<String, String> map = new TreeMap<>();

                for (int j = 4; j < 4 + words.get(w).length(); j++){

                    if ( j == 4){
                        mass[4][j] = String.valueOf(words.get(w).charAt(j-4)) + "" + "1";
                        continue;
                    }
                    mass[4][j] = String.valueOf(words.get(w).charAt(j-4));
                }
                map.put("1" +  " - гориз.", String.valueOf(words.get(w)));

                words.remove(w);

                int sch = 2;

                while ( words.size() != 0){

                    int b = rand.nextInt(words.size());
                    boolean forr = false;

                    for ( int i = 0; i < words.get(b).length(); i++){ // берем букву из слова

                        Character bukva = words.get(b).charAt(i);// берем букву из слова

                        for ( int f = 0; f < 30; f++){// гуляем по матрице и ищем совпадение

                            for ( int j = 0; j < 30; j++){// гуляем по матрице и ищем совпадение

                                if (mass[f][j].charAt(0) == bukva){

                                    if (j - 1 >= 0 && j + 1 < 30  && (mass[f][j - 1] != "." || mass[f][j + 1] != ".") ){ // слева справо, ошибка

                                        int low_wall_in_word = f + (words.get(b).length() - i - 1);// 30 может всплыть
                                        int high_wall_in_word = f - i - 1; // -1 может всплыть

                                        boolean vert = true;
                                        if (low_wall_in_word < 30 && high_wall_in_word >= 0) {

                                            for (int y = low_wall_in_word; y > high_wall_in_word; y--) { // смотрим можем ли по вертикали вставать слово, ошибка

                                                if (
                                                        j - 1 >= 0
                                                                && j + 1 < 30
                                                                && (mass[y][j - 1] != "." || mass[y][j + 1] != ".")
                                                                && y != f
                                                ){
                                                    vert = false;
                                                    break;
                                                }

                                                if (low_wall_in_word + 1 < 30){

                                                    if (mass[low_wall_in_word + 1 ][j] != "."){
                                                        vert = false;
                                                        break;
                                                    }

                                                }

                                                if (high_wall_in_word >= 0){

                                                    if (mass[high_wall_in_word][j] != "."){
                                                        vert = false;
                                                        break;
                                                    }

                                                }

                                                if (mass[y][j] != ".") {

                                                    if (y == f) {
                                                        continue;
                                                    }

                                                    vert = false;
                                                    break;

                                                }
                                            }

                                            if (vert == true) {

                                                for (int y = f + (words.get(b).length() - i - 1), p = 1; y > f - i - 1; y--, p++) { // вставляем слово по вертикали, ошибка

                                                    if (mass[y][j].length() == 2){
                                                        if(y == f - i){
                                                            map.put(String.valueOf(mass[y][j].charAt(1)) + " - верт.", String.valueOf(words.get(b)));
                                                            mass[y][j] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" +  String.valueOf(mass[y][j].charAt(1));
                                                            sch++;
                                                        }
                                                        else {
                                                            mass[y][j] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" +  String.valueOf(mass[y][j].charAt(1));
                                                            sch++;
                                                        }
                                                        continue;
                                                    }
                                                    if (mass[y][j].length() == 3){
                                                        if(y == f - i){
                                                            map.put(String.valueOf(mass[y][j].charAt(1)) + "" + String.valueOf(mass[y][j].charAt(2)) + " - верт.", String.valueOf(words.get(b)));
                                                            mass[y][j] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" +  String.valueOf(mass[y][j].charAt(1)) + String.valueOf(mass[y][j].charAt(2));
                                                            sch++;
                                                        }
                                                        else{
                                                            mass[y][j] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" +  String.valueOf(mass[y][j].charAt(1)) + String.valueOf(mass[y][j].charAt(2));
                                                            sch++;
                                                        }
                                                        continue;
                                                    }

                                                    if (y ==  f -i ){
                                                        map.put(Integer.toString(sch) + " - верт.", String.valueOf(words.get(b)));
                                                        mass[y][j] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" +  Integer.toString(sch) ;
                                                        sch++;
                                                        continue;
                                                    }
                                                    mass[y][j] = String.valueOf(words.get(b).charAt(words.get(b).length() - p));

                                                }

                                                forr = true;

                                            }
                                        }
                                    }
                                    if(f+1 < 30 && f-1 >=0) {
                                        if (f - 1 >= 0 && f + 1 < 30 && mass[f - 1][j] != "." || mass[f + 1][j] != ".") { // верх низ, ошибка

                                            boolean goriz = true;
                                            int left_wall_in_word = j - i - 1;
                                            int right_wall_in_word = j + (words.get(b).length() - i - 1);

                                            if (j + (words.get(b).length() - i - 1) < 30 && j - i - 1 >= 0) {

                                                for (int y = j + (words.get(b).length() - i - 1); y > j - i - 1; y--) {

                                                    if (
                                                            f - 1 >= 0
                                                                    && f + 1 < 30
                                                                    && (mass[f - 1][y] != "." || mass[f + 1][y] != ".")
                                                                    && y != j
                                                    ) {

                                                        goriz = false;
                                                        break;
                                                    }

                                                    if (left_wall_in_word >= 0) {

                                                        if (mass[f][left_wall_in_word] != ".") {
                                                            goriz = false;
                                                            break;
                                                        }

                                                    }

                                                    if (right_wall_in_word + 1 < 30) {

                                                        if (mass[f][right_wall_in_word + 1] != ".") {
                                                            goriz = false;
                                                            break;
                                                        }

                                                    }

                                                    if (mass[f][y] != ".") {
                                                        if (y == j) {

                                                            continue;

                                                        }

                                                        goriz = false;
                                                        break;

                                                    }

                                                }

                                                if (goriz == true) {

                                                    for (int y = j + (words.get(b).length() - i - 1), p = 1; y > j - i - 1; y--, p++) {

                                                        if (mass[f][y].length() == 2) {
                                                            if (y == j - i) {
                                                                map.put(String.valueOf(mass[f][y].charAt(1)) + " - гориз.", String.valueOf(words.get(b)));
                                                                mass[f][y] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" + String.valueOf(mass[f][y].charAt(1));
                                                                sch++;
                                                            }
                                                            else {
                                                                mass[f][y] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" + String.valueOf(mass[f][y].charAt(1));
                                                                sch++;
                                                            }
                                                            continue;
                                                        }
                                                        if (mass[f][y].length() == 3) {
                                                            if( y == j - i){
                                                                map.put(String.valueOf(mass[f][y].charAt(1)) + "" + String.valueOf(mass[f][y].charAt(2)) + " - гориз.", String.valueOf(words.get(b)));
                                                                mass[f][y] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" + String.valueOf(mass[f][y].charAt(1)) + String.valueOf(mass[f][y].charAt(2));
                                                                sch++;
                                                            }
                                                            else {
                                                                mass[f][y] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" + String.valueOf(mass[f][y].charAt(1)) + String.valueOf(mass[f][y].charAt(2));
                                                                sch++;
                                                            }
                                                            continue;
                                                        }
                                                        if (y == j - i) {
                                                            map.put(Integer.toString(sch) + " - гориз.", String.valueOf(words.get(b)));
                                                            mass[f][y] = String.valueOf(words.get(b).charAt(words.get(b).length() - p)) + "" + Integer.toString(sch);
                                                            sch++;
                                                            continue;
                                                        }
                                                        mass[f][y] = String.valueOf(words.get(b).charAt(words.get(b).length() - p));

                                                    }

                                                    forr = true;
                                                }
                                            }

                                        }
                                    }
                                }

                                if(forr == true){
                                    break;
                                }
                            }

                            if(forr == true){
                                break;
                            }
                        }

                        if(forr == true){
                            break;
                        }
                    }
                    words.remove(b);

                    forr = false;
                }

                JTextField[][] textFieldMatrix;
                textFieldMatrix = new JTextField[mass.length][mass[0].length];


                for (int i = 0; i < mass.length; i++) {

                    for (int j = 0; j < mass[i].length; j++) {
                        // Создаем текстовое поле для каждой ячейки кроссворда

                        JTextField textField = new JTextField();
                        textField.setTransferHandler(null);

                        final int[] ind_x = {i};
                        final int[] ind_y = {j};
                        textField.addKeyListener(new KeyAdapter() {
                            public void keyTyped(KeyEvent e) {
                                if (textField.getText().length() >= 1) // limit textfield to 3 characters
                                    e.consume();
                                }
                            @Override
                            public void keyPressed(KeyEvent e) {
                                if (e.getKeyCode() == KeyEvent.VK_DOWN){
                                    if ((ind_x[0] - 1) < 0){
                                        ind_x[0] = 30;
                                    }
                                    textFieldMatrix[ind_x[0] + 1][ind_y[0]].requestFocusInWindow();
                                }
                                if (e.getKeyCode() == KeyEvent.VK_UP){
                                    if ((ind_x[0] - 1) < 0){
                                        ind_x[0] = 30;
                                    }
                                    textFieldMatrix[ind_x[0] - 1][ind_y[0]].requestFocusInWindow();
                                }
                                if (e.getKeyCode() == KeyEvent.VK_LEFT){
                                    if ((ind_y[0] - 1) < 0){
                                        ind_y[0] = 30;
                                    }
                                    textFieldMatrix[ind_x[0]][ind_y[0] - 1].requestFocusInWindow();
                                }
                                if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                                    if ((ind_x[0] - 1) < 0){
                                        ind_y[0] = 30;
                                    }
                                    textFieldMatrix[ind_x[0]][ind_y[0] + 1].requestFocusInWindow();
                                }
                            }
                        });

                        textField.addKeyListener(new KeyAdapter() {
                            public void keyTyped(KeyEvent e) {
                                if (textField.getText().length() >= 1 ) // limit textfield to 3 characters
                                    e.consume();
                            }

                        });

                        textField.setBounds( (int)((j+18) * 25 * coef_w), (int)((i+1) * 25 * coef_w), (int)(25 * coef_w), (int)(25 * coef_w));

                        // Добавляем текстовое поле на панель

                        jFrame.add(textField);
                        if (mass[i][j] == "."){
                            textField.setBackground(Color.cyan);
                            textField.setEditable(false);
                        }

                        if(mass[i][j].length() == 2 ){
                            textField.setText(String.valueOf(mass[i][j].charAt(1)));
                            int a = i;
                            int b = j;
                            textField.addFocusListener(new FocusListener() {
                                @Override
                                public void focusGained(FocusEvent e) {
                                    if (textField.getText().equals(String.valueOf(mass[a][b].charAt(1)))){
                                        textField.setText("");
                                    }
                                }

                                @Override
                                public void focusLost(FocusEvent e) {
                                    if (textField.getText().equals("")){
                                        textField.setText(String.valueOf(mass[a][b].charAt(1)));
                                    }
                                }
                            });
                        }
                        if(mass[i][j].length() == 3 ){
                            textField.setText(String.valueOf(mass[i][j].charAt(1)) + "" + String.valueOf(mass[i][j].charAt(2)));
                            int a = i;
                            int b = j;
                            textField.addFocusListener(new FocusListener() {
                                @Override
                                public void focusGained(FocusEvent e) {
                                    if (textField.getText().equals(String.valueOf(mass[a][b].charAt(1)) + "" + String.valueOf(mass[a][b].charAt(2)))){
                                        textField.setText("");
                                    }
                                }

                                @Override
                                public void focusLost(FocusEvent e) {
                                    if (textField.getText().equals("")){
                                        textField.setText(String.valueOf(mass[a][b].charAt(1)) + "" + String.valueOf(mass[a][b].charAt(2)));
                                    }
                                }
                            });

                        }
                        textField.setHorizontalAlignment(JTextField.CENTER);

                        // Сохраняем ссылку на текстовое поле в матрице
                        textFieldMatrix[i][j] = textField;

                        // Устанавливаем значение ячейки кроссворда
                        //textField.setText(String.valueOf(mass[i][j]));

                    }
                }
                JLabel label = new JLabel("");
                jFrame.add(label);
                label.setBounds((int)(20 * coef_w), (int)(50 * coef_h), (int)(400 * coef_w), (int)(250 * coef_h));
                jFrame.setLayout(null);

                JButton Rez = new JButton("Проверить результат");
                Rez.setFont(fontg);
                Rez.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int sch_vvod = 0;
                        int sch_ishodnoe = 0;
                        Boolean itog = true;
                        Boolean proverka = true;
                        for ( int i = 0; i < 30; i++){
                            for ( int j = 0; j < 30; j++){
                                //ЫЫЫЫЫ
                                if(mass[i][j].charAt(0) != '.'){
                                    sch_ishodnoe++;
                                }

                                String input = textFieldMatrix[i][j].getText();
                                if( mass[i][j].charAt(0) != '.'){
                                    if(input.length() == 0){
                                        itog = false;
                                        proverka = false;
                                        continue;
                                    }
                                    if ( input.charAt(0) == (mass[i][j].charAt(0))){
                                        sch_vvod++;
                                    }
                                    else{
                                        itog = false;
                                    }
                                }
                            }
                        }
                        if ( itog == true){
                            c = false;
                            JFrame pobeda = new JFrame();
                            pobeda.setBounds(0, 0, dimension.width, dimension.height);
                            pobeda.setTitle("MaFWord");
                            Image image = new ImageIcon("img/MathWord.png").getImage();
                            pobeda.setIconImage(image);
                            pobeda.setVisible(true);
                            JButton return1 = new JButton("Вернуться на стартовое меню");
                            pobeda.add(return1);
                            pobeda.add(myComponent);
                            pobeda.setLayout(null);
                            return1.setBounds( (int)(20 * coef_w), (int)(25 * coef_h), (int)(215 * coef_w), (int)(40 * coef_h));
                            return1.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    b = true;
                                    c = true;
                                    jFrame.getContentPane().removeAll();
                                    jFrame.repaint();
                                    pobeda.dispose();
                                    StartMenu();
                                }
                            });
                            jFrame.add(Rez);
                            jFrame.setLayout(null);
                            Rez.setBounds(1000, 0, 60, 20);
                            //Тут пишется в окне победа
                        }

                        else{
                            if(proverka == false)
                                label.setText("Вы разгадали - " + String.valueOf(sch_vvod*100/sch_ishodnoe) + " % заданий.");
                            else label.setText("Не все задания решены верно!");

                        }
                    }

                });
                jFrame.add(Rez);
                jFrame.setLayout(null);
                jFrame.add(myComponent);
                jFrame.setLayout(null);

                Rez.setBounds( (int)(45 * coef_w),  (int)(765 * coef_h), (int)(160 * coef_w), (int)(25 * coef_h));
                Font fontb = new Font("Arial Black", Font.BOLD, dimension.width*dimension.height/110592);
                int i = 0, j = 1276;



                for(Map.Entry entry:map.entrySet()){
                    JButton button = new JButton(String.valueOf(entry.getKey()));

                    button.setFont(fontb);
                    jFrame.add(button);
                    jFrame.setLayout(null);
                    if( i*40 > 761){
                        i = 0;
                        j +=120;
                    }
                    button.setBounds((int)(j * coef_w), (int)(25 + i*37 * coef_h), (int)(120 * coef_w), (int)(37 * coef_h));

                    i++;
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e)  {
                            for(int i = 0; i < 30; i++) {
                                for (int j = 0; j < 30; j++) {
                                    if ( mass[i][j].equals(".") == false){
                                        textFieldMatrix[i][j].setBackground(Color.WHITE);
                                    }
                                }
                            }
                            int s = 0;
                            String chislo = "";
                            while (String.valueOf(entry.getKey()).charAt(s) != ' '){
                                chislo += String.valueOf(entry.getKey()).charAt(s);
                                s++;
                            }
                            String napravlenie = String.valueOf(String.valueOf(entry.getKey()).charAt(s+3));
                            for(int i = 0; i < 30; i++){
                                for( int j = 0; j < 30; j++){
                                    if(chislo.equals(mass[i][j].substring(1, mass[i][j].length()))){
                                        if(napravlenie.equals("г")){
                                            int k = j;
                                            while(k < 30 && mass[i][k].equals(".") == false){
                                                textFieldMatrix[i][k].setBackground(Color.orange);
                                                k++;
                                            }
                                        }
                                        else{
                                            int k = i;
                                            while(k < 30 && mass[k][j].equals(".") == false){
                                                textFieldMatrix[k][j].setBackground(Color.orange);
                                                k++;
                                            }
                                        }
                                    }
                                }
                            }

                            for (Map.Entry pop:map_op.entrySet()){
                                if(String.valueOf(entry.getValue()).equals(String.valueOf(pop.getKey()))){
                                    Value = String.valueOf(pop.getValue());
                                }
                            }
                            label.setText(Value);
                        }
                    });
                }

                JButton Return = new JButton("Главное меню");

                Font fontg = new Font("Arial Black", Font.BOLD, dimension.width*dimension.height/150000);
                Return.setFont(fontg);
                Return.setBounds( (int)(20 * coef_w), (int)(25 * coef_h), (int)(120 * coef_w), (int)(40 * coef_h)); //КРОССПЛАТФОРМЕННОСТЬ
                jFrame.add(Return);
                Return.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        b = true;
                        jFrame.getContentPane().removeAll();
                        jFrame.repaint();
                        StartMenu();

                    }
                });

                ImageIcon icon = new ImageIcon("img/nota.jpg");
                Image scaledImg = icon.getImage().getScaledInstance((int)(20 * coef_w), (int)(20 * coef_h),  Image.SCALE_SMOOTH); //КРОССПЛАТФОРМЕННОСТЬ
                icon = new ImageIcon(scaledImg);

                JButton music = new JButton(icon);
                music.setBounds( (int)(10 * coef_w), (int)(765 * coef_h), (int)(25 * coef_w), (int)(25 * coef_w));
                music.setHorizontalTextPosition(JButton.CENTER);
                music.setVerticalTextPosition(JButton.CENTER);

                jFrame.add(music);

                music.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String path = "img/kev-rowe-a-great-calm.wav";
                        File filemusic = new File(path);

                        try(AudioInputStream au = AudioSystem.getAudioInputStream(filemusic);) {
                            if (!a){
                                clip.stop();
                                a = true;
                            } else {
                                clip = AudioSystem.getClip();
                                clip.open(au);
                                clip.start();
                                a = false;
                            }
                        }
                        catch (Exception a){
                        }
                    }

                });
            }
        });
        JButton music = new JButton("Музыка");
        music.setBounds(dimension.width*355/384, dimension.height*5/216, dimension.width*7/96, dimension.height*25/864);
        music.setFont(fontg);
        jFrame.add(music);
        music.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = "img/kev-rowe-a-great-calm.wav";
                File filemusic = new File(path);
                try(AudioInputStream au = AudioSystem.getAudioInputStream(filemusic);) {
                    if (!a){
                        clip.stop();
                        a = true;
                    } else {
                        clip = AudioSystem.getClip();
                        clip.open(au);
                        clip.start();
                        a = false;
                    }
                }
                catch (Exception a){
                }
            }
        });

        jFrame.setLayout(null);
    }

    public static void main(String[] args) throws IOException {
        StartMenu();
    }



    static class MyComponent extends  JComponent{
        @Override
        protected void paintComponent(Graphics g){

            Font font = new Font("Courier New", Font.BOLD, 20);
            Graphics2D g2 = (Graphics2D)g;

            g2.setFont(font);

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension dimension = toolkit.getScreenSize();

            if (c == false){
                ImageIcon pobeda = new ImageIcon("img/победа.png");
                Image w = pobeda.getImage();
                Image v = w.getScaledInstance(dimension.width, dimension.height - dimension.height/6,  java.awt.Image.SCALE_SMOOTH);
                pobeda = new ImageIcon(v);
                g2.drawImage(pobeda.getImage(), 0 , dimension.height/12, null);

            }
            else{
                if(b == false){
                    ImageIcon klava = new ImageIcon("img/панда2.png");
                    Image h = klava.getImage();
                    Image d = h.getScaledInstance(dimension.width*275/768, dimension.height*125/216,  java.awt.Image.SCALE_SMOOTH);
                    klava = new ImageIcon(d);
                    g2.drawImage(klava.getImage(), -dimension.width*10/384 , dimension.height*85/216, null);
                }
                else{
                    ImageIcon trava = new ImageIcon("img/trava.png");
                    Image itrava = trava.getImage();
                    Image fgh = itrava.getScaledInstance(dimension.width, dimension.height*25/108,  java.awt.Image.SCALE_SMOOTH);
                    trava = new ImageIcon(fgh);
                    g2.drawImage(trava.getImage(), 0 , dimension.height - dimension.height/5, null);

                    ImageIcon Nikita = new ImageIcon("img/sk.png");
                    Image image4 = Nikita.getImage();
                    Image newimg = image4.getScaledInstance(dimension.width*591/1536, dimension.height*40/27,  java.awt.Image.SCALE_SMOOTH);
                    Nikita = new ImageIcon(newimg);
                    g2.drawImage(Nikita.getImage(), dimension.width*125/192 , -dimension.height*25/216, null);

                    ImageIcon Artem = new ImageIcon("img/av.png");
                    Image image0 = Artem.getImage();
                    Image qwe = image0.getScaledInstance(dimension.width*591/1536, dimension.height*40/27,  java.awt.Image.SCALE_SMOOTH);
                    Artem = new ImageIcon(qwe);
                    g2.drawImage(Artem.getImage(), -dimension.width*25/768 , -dimension.height*55/432, null);
                }
            }


        }
    }
    static JFrame getFrame(){

        JFrame jFrame = new JFrame() {};

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        Image image = new ImageIcon("img/MathWord.png").getImage();
        jFrame.setIconImage(image);

        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setBounds(0, 0, dimension.width, dimension.height);
        jFrame.setTitle("MaFWord");

        return jFrame;
    }
}

