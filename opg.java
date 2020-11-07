import java.io.*;
import java.util.Stack;


/**
 * 创建一个二维数组用来存储优先级关系
 *    +  *  i  (  )  #
 * +  1 -1 -1 -1  1  1
 * *  1  1 -1 -1  1  1
 * i  1  1  2  2  1  1
 * ( -1 -1 -1 -1  0  2
 * )  1  1  2  2  1  1
 * # -1 -1 -1 -1  2  0
 * 其中，1为大于，0为等于，-1为小于，2为不存在
 */
public class opg {
    public static int[][] priority = {
            { 1,-1,-1,-1, 1, 1},
            { 1, 1,-1,-1, 1, 1},
            { 1, 1, 2, 2, 1, 1},
            {-1,-1,-1,-1, 0, 2},
            { 1, 1, 2, 2, 1, 1},
            {-1,-1,-1,-1, 2, 0}
    };

    static int location = 0;

    public static void main(String[] args) throws Exception{
        File file = new File(args[0]);
        //File file = new File("src/work.txt");
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        String sentence = "#"+bReader.readLine();
        bReader.close();
        sentence.replace("/r/n","#");
        sentence.replaceAll("\\+","0");
        sentence.replaceAll("\\*","1");
        sentence.replaceAll("i","2");
        sentence.replaceAll("\\(","3");
        sentence.replaceAll("\\)","4");
        sentence.replaceAll("#","5");

        Stack<Character> OPND = new Stack<Character>();//存运算数
        Stack<Character> OPTR = new Stack<Character>();//存运算符

        int length = sentence.length();
        char c = ' ';
        char last_c = c;


        while (true){

            c = getChar(sentence, location);

            //排除序列中不应相同的字符情况
            switch (c){
                case '2': if (last_c=='2'){
                            System.out.println("E");
                            return;
                        }
                case '0':
                case '1': if (last_c=='0' || last_c=='1'){
                            System.out.println("E");
                            return;
                        }
            }

            //进行真正的分析算法
            switch (c){
                case '2':
                    OPND.add(c);
                    System.out.println("I"+c);
                    System.out.println("R");
                    last_c = c;
                    break;
                case '0':
                case '1':
                case '3':
                case '4':
                    //如果没有运算数的话
                    if (OPND.empty()){
                        System.out.println("E");
                        return;
                    }
                    //如果没有运算符的话
                    if (OPTR.size()==1){
                        OPTR.push(c);
                        System.out.println("I"+c);
                        last_c = c;
                        break;
                    }
                    //咱家里有运算符啦
                    else {
                        while (true){
                            char theta = OPTR.peek();

                            //判断优先级
                            int cmp = compare(theta,c);

                            //cmp==1， 即栈内大于栈外的运算符的时候，需要重复循环，其他的不需要
                           if (cmp==1){
                                if (OPND.size()<2){
                                    System.out.println("RE");
                                    return;
                                }

                                char E1 = OPND.pop();
                                char E2 = OPND.pop();
                                theta = OPTR.pop();
                                boolean transfer = statue(E1, theta, E2);
                                if (transfer){
                                    last_c = E1;//规约后算式对的最后一个就是I了
                                    OPND.push(E1);
                                    OPTR.push(c);
                                    System.out.println("I"+c);
                                    System.out.println("R");
                                }
                                else {
                                    System.out.println("RE");
                                    return;
                                }
                            }
                           //这时只能是左括号和右括号
                           else if (cmp == 0) {
                               last_c = '2';//这时式子最右侧的还是运算数
                               OPTR.pop();//把左括号扔出来
                               System.out.println("I"+c);
                               System.out.println("R");
                               break;//这时循环需要结束！！！
                           }
                           else if (cmp == -1) {
                               OPTR.push(c);
                               System.out.println("I"+c);
                               break;//这时循环需要结束！！！
                           }
                           //读入到一些奇怪的情况了诶
                           else {
                               System.out.println("E");
                               return;
                           }
                        }
                    }
                    break;
                case '5':
                    if (OPTR.empty()){
                        last_c = c;
                        OPTR.push(c);
                    }
                    //这里应该是正常结束啦！
                    else {
                        return;
                    }
                    break;
            }


        }


    }

    public static char getChar(String s, int i){
        location = i+1;
        return peekChar(s, i);
    }

    public static char peekChar(String s, int i){
        return s.charAt(i);
    }

    public static int compare(char a, char b){
        return priority[Integer.parseInt(String.valueOf(a))][Integer.parseInt(String.valueOf(b))];
    }

    public static boolean statue(char E1, char theta, char E2){
        StringBuilder s = new StringBuilder();
        s.append(E1).append(theta).append(E2);
        if (s.toString().equals("202") || s.toString().equals("212")){
            return true;
        }
        return false;
    }
}
