import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;


public class operation {

    private Stack<Long> numberStack = null;//数字栈
    private Stack<Character> symbolStack = null;//符号栈

    public  long caculate(String numStr) {
        boolean exp=true;//判断运算式是否合法
        numStr = removeStrSpace(numStr); // 去除空格
        if (numStr.length() > 1 && !"=".equals(numStr.charAt(numStr.length() - 1) + "")) {
            numStr += "=";
        }
        if (!isStandard(numStr)) {
            System.err.println("错误：算术表达式有误！"); // 检查表达式是否合法
            return 0;
        }
        numberStack = new Stack<Long>(); // 初始化栈
        symbolStack = new Stack<Character>();
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < numStr.length(); i++) {
            char ch = numStr.charAt(i); // 获取一个字符
            if (isNumber(ch)) { // 判断当前字符是否为数字
                temp.append(ch); 
            } else { 
                String tempStr = temp.toString(); 
                if (!tempStr.isEmpty()) {
                    long num = Long.parseLong(tempStr); 
                    numberStack.push(num); // 将数字压栈
                    temp = new StringBuffer(); // 重置数字缓存
                }
                // 判断运算符的优先级
                while (!comparePri(ch) && !symbolStack.empty()) {
                    long b = numberStack.pop(); 
                    long a = numberStack.pop();
                    long midtemp=0;
                    switch ((char) symbolStack.pop()) {
                        case '+':
                            numberStack.push(a + b);
                            break;
                        case '-':
                            midtemp = a - b;
                            if(midtemp<0) {   //判断减法结果是否为负数
                                midtemp=0;
                                exp = false;
                            }
                            numberStack.push(midtemp);
                            break;
                        case '*':
                            numberStack.push(a * b);
                            break;
                        case '/':     //判断除法是否合法，且能整除
                            if(b==0) {
                                b=1;
                                exp = false;
                            }
                            midtemp = a / b;
                            if(midtemp*b!=a) 
                            	exp = false;
                            numberStack.push(midtemp);
                            break;
                        default:
                            break;
                    }
                }
                if (ch != '=') {
                    symbolStack.push(new Character(ch)); // 符号入栈
                    if (ch == ')') { // 去右括号
                        symbolStack.pop();
                        symbolStack.pop();
                    }
                }
            }
        }
        // 返回计算结果
        if(exp) 
        	return numberStack.pop();
        else 
        	return 999999;   
    }

     //去除字符串中的所有空格
    String removeStrSpace(String str) {
        return str != null ? str.replaceAll(" ", "") : "";
    }

   
    //检查算术表达式的基本合法性，符合返回true，否则false
    private boolean isStandard(String numStr) {
        if (numStr == null || numStr.isEmpty()) // 表达式不能为空
            return false;
        Stack<Character> stack = new Stack<Character>(); // 用来保存括号，检查左右括号是否匹配
        boolean b = false; 
        for (int i = 0; i < numStr.length(); i++) {
            char n = numStr.charAt(i);
            // 判断字符是否合法
            if (!(isNumber(n) || "(".equals(n + "") || ")".equals(n + "")
                    || "+".equals(n + "") || "-".equals(n + "")
                    || "*".equals(n + "") || "/".equals(n + "")
                    || "=".equals(n + ""))) {
                return false;
            }
            if ("(".equals(n + "")) {
                stack.push(n);
            }
            if (")".equals(n + "")) { 
                if (stack.isEmpty() || !"(".equals((char) stack.pop() + "")) // 括号是否匹配
                    return false;
            }
            // 检查是否有多个'='号
            if ("=".equals(n + "")) {
                if (b)
                    return false;
                b = true;
            }
        }
        // 可能会有缺少右括号的情况
        if (!stack.isEmpty())
            return false;
        // 检查'='号是否不在末尾
        if (!("=".equals(numStr.charAt(numStr.length() - 1) + "")))
            return false;
        return true;
    }

   //判断数字
    private boolean isNumber(char num) {
        if (num >= '0' && num <= '9')
            return true;
        return false;
    }

   //比较优先级
    private boolean comparePri(char symbol) {
        if (symbolStack.empty()) { 
            return true;
        }
        char top = (char) symbolStack.peek(); // 查看堆栈顶部的对象
        if (top == '(') {
            return true;
        }
        switch (symbol) {
            case '(': // 优先级最高
                return true;
            case '*': {
                if (top == '+' || top == '-') // 优先级比+和-高
                    return true;
                else
                    return false;
            }
            case '/': {
                if (top == '+' || top == '-') // 优先级比+和-高
                    return true;
                else
                    return false;
            }
            case '+':
                return false;
            case '-':
                return false;
            case ')': // 优先级最低
                return false;
            case '=': // 结束符
                return false;
            default:
                break;
        }
        return true;
    }


    public static void main(String args[]) {

        int num;
        int writer=0;
        int[] amount = {3,4,5};//存放操作数的数组
        char[] symbol = {'+','-','*','/'};//存放运算符的数组
        System.out.println("Please input num:");
        Scanner N = new Scanner(System.in);
        num = N.nextInt();
        for(int i=0;i<num;i++)
        {
            int m = (int)(Math.random()*amount.length);
            int k = amount[m]; //操作数的个数
            String len = new String();
            //System.out.println(amount[m]);
            try {
                //如果文件存在，则追加内容；如果文件不存在，则创建文件
                File f1 = new File("result.txt");
                FileWriter fw = new FileWriter(f1, true);

                PrintWriter pw = new PrintWriter(fw);
                if(writer==0)
                {
                    pw.println("201571030130");
                    writer=1;
                }
                switch(k)
                {
                    case 3://三个操作数
                        int a = (int)(Math.random()*100)+1;//操作数a,b,c
                        int b = (int)(Math.random()*100)+1;
                        int c = (int)(Math.random()*100)+1;
                        int x = (int)(Math.random()*symbol.length);//操作符x,y
                        int y = (int)(Math.random()*symbol.length);
                        int bracket = (int)(Math.random()*3);//括号,由于该式中只有三个操作数，故只考虑两种括号的位置
                        if(bracket==0)//0表示没有括号
                        {
                            len=String.valueOf(a)+String.valueOf(symbol[x])+String.valueOf(b)+String.valueOf(symbol[y]+String.valueOf(c)+"= ");
                        }
                        else if(bracket==1)//1表示括号在左半部
                        {
                            len="("+String.valueOf(a)+String.valueOf(symbol[x])+String.valueOf(b)+")"+String.valueOf(symbol[y]+String.valueOf(c)+"= ");
                        }
                        else//2表示括号在右半部
                        {
                            len=String.valueOf(a)+String.valueOf(symbol[x])+"("+String.valueOf(b)+String.valueOf(symbol[y]+String.valueOf(c)+")"+"= ");
                        }
                        operation cal = new operation();
                        long result=cal.caculate(len);
                        if(result!=999999)
                        {
                            System.out.println(len+result);
                            pw.println(len+result);
                        }
                        else
                            i--;
                        break;

                    case 4:
                        int a1 = (int)(Math.random()*100)+1;
                        int b1 = (int)(Math.random()*100)+1;
                        int c1 = (int)(Math.random()*100)+1;
                        int d1 = (int)(Math.random()*100)+1;
                        int x1 = (int)(Math.random()*symbol.length);
                        int y1 = (int)(Math.random()*symbol.length);
                        int z1 = (int)(Math.random()*symbol.length);
                        int bracket1 = (int)(Math.random()*4);//括号,注意四个式子中括号为2个，此时有3种情况
                        if(bracket1==0)//0表示没有括号
                        {
                            len=String.valueOf(a1)+String.valueOf(symbol[x1])+String.valueOf(b1)+String.valueOf(symbol[y1]+String.valueOf(c1)+String.valueOf(symbol[z1])+String.valueOf(d1)+"= ");
                        }
                        else if(bracket1==1)
                        {
                            len="("+String.valueOf(a1)+String.valueOf(symbol[x1])+String.valueOf(b1)+")"+String.valueOf(symbol[y1]+"("+String.valueOf(c1)+String.valueOf(symbol[z1])+String.valueOf(d1)+")"+"= ");
                        }
                        else if(bracket1==2)
                        {
                            len="("+"("+String.valueOf(a1)+String.valueOf(symbol[x1])+String.valueOf(b1)+")"+String.valueOf(symbol[y1]+String.valueOf(c1)+")"+String.valueOf(symbol[z1])+String.valueOf(d1)+"= ");
                        }
                        else
                        {
                            len=String.valueOf(a1)+String.valueOf(symbol[x1])+"("+String.valueOf(b1)+String.valueOf(symbol[y1]+"("+String.valueOf(c1)+String.valueOf(symbol[z1])+String.valueOf(d1)+")"+")"+"= ");
                        }
                        operation cal1 = new operation();
                        long result1=cal1.caculate(len);
                        if(result1!=999999)
                        {
                            System.out.println(len+result1);
                            pw.println(len+result1);
                        }

                        else
                            i--;
                        break;

                    case 5:
                        int a2 = (int)(Math.random()*100)+1;
                        int b2 = (int)(Math.random()*100)+1;
                        int c2 = (int)(Math.random()*100)+1;
                        int d2 = (int)(Math.random()*100)+1;
                        int e2 = (int)(Math.random()*100)+1;
                        int x2 = (int)(Math.random()*symbol.length);
                        int y2 = (int)(Math.random()*symbol.length);
                        int z2 = (int)(Math.random()*symbol.length);
                        int o2 = (int)(Math.random()*symbol.length);
                        int bracket2 = (int)(Math.random()*9);//括号,对于有4个运算符的式子，这里只考虑了加2个括号的其中8中情况
                        if(bracket2==0)//无括号
                        {
                            len=String.valueOf(a2)+String.valueOf(symbol[x2])+String.valueOf(b2)+String.valueOf(symbol[y2]+String.valueOf(c2)+String.valueOf(symbol[z2])+String.valueOf(d2)+String.valueOf(symbol[o2])+String.valueOf(e2)+"= ");
                        }
                        else if(bracket2==1)
                        {
                            len="("+String.valueOf(a2)+String.valueOf(symbol[x2])+String.valueOf(b2)+")"+String.valueOf(symbol[y2]+"("+String.valueOf(c2)+String.valueOf(symbol[z2])+String.valueOf(d2)+")"+String.valueOf(symbol[o2])+String.valueOf(e2)+"= ");
                        }
                        else if(bracket2==2)
                        {
                            len="("+String.valueOf(a2)+String.valueOf(symbol[x2])+String.valueOf(b2)+")"+String.valueOf(symbol[y2]+String.valueOf(c2)+String.valueOf(symbol[z2])+"("+String.valueOf(d2)+String.valueOf(symbol[o2])+String.valueOf(e2)+")"+"= ");
                        }
                        else if(bracket2==3)
                        {
                            len=String.valueOf(a2)+String.valueOf(symbol[x2])+"("+String.valueOf(b2)+String.valueOf(symbol[y2]+String.valueOf(c2)+")"+String.valueOf(symbol[z2])+"("+String.valueOf(d2)+String.valueOf(symbol[o2])+String.valueOf(e2)+")"+"= ");
                        }
                        else if(bracket2==4)
                        {
                            len="("+"("+String.valueOf(a2)+String.valueOf(symbol[x2])+String.valueOf(b2)+")"+String.valueOf(symbol[y2]+String.valueOf(c2)+")"+String.valueOf(symbol[z2])+String.valueOf(d2)+String.valueOf(symbol[o2])+String.valueOf(e2)+"= ");
                        }
                        else if(bracket2==5)
                        {
                            len="("+"("+String.valueOf(a2)+String.valueOf(symbol[x2])+String.valueOf(b2)+")"+String.valueOf(symbol[y2]+String.valueOf(c2)+String.valueOf(symbol[z2])+String.valueOf(d2)+")"+String.valueOf(symbol[o2])+String.valueOf(e2)+"= ");
                        }
                        else if(bracket2==6)
                        {
                            len="("+String.valueOf(a2)+String.valueOf(symbol[x2])+"("+String.valueOf(b2)+String.valueOf(symbol[y2]+String.valueOf(c2)+")"+String.valueOf(symbol[z2])+String.valueOf(d2)+")"+String.valueOf(symbol[o2])+String.valueOf(e2)+"= ");
                        }
                        else if(bracket2==7)
                        {
                            len=String.valueOf(a2)+String.valueOf(symbol[x2])+"("+String.valueOf(b2)+String.valueOf(symbol[y2]+"("+String.valueOf(c2)+String.valueOf(symbol[z2])+String.valueOf(d2)+")"+")"+String.valueOf(symbol[o2])+String.valueOf(e2)+"= ");
                        }
                        else
                        {
                            len=String.valueOf(a2)+String.valueOf(symbol[x2])+"("+String.valueOf(b2)+String.valueOf(symbol[y2]+String.valueOf(c2)+String.valueOf(symbol[z2])+"("+String.valueOf(d2)+String.valueOf(symbol[o2])+String.valueOf(e2)+")"+")"+"= ");
                        }
                        operation cal2 = new operation();
                        long result2=cal2.caculate(len);
                        if(result2!=999999)
                        {
                            System.out.println(len+result2);
                            pw.println(len+result2);
                        }else i--;

                        break;
                }
                pw.flush();
                pw.close();
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

}
