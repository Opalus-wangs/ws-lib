import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;


public class operation {

    private Stack<Long> numberStack = null;//����ջ
    private Stack<Character> symbolStack = null;//����ջ

    public  long caculate(String numStr) {
        boolean exp=true;//�ж�����ʽ�Ƿ�Ϸ�
        numStr = removeStrSpace(numStr); // ȥ���ո�
        if (numStr.length() > 1 && !"=".equals(numStr.charAt(numStr.length() - 1) + "")) {
            numStr += "=";
        }
        if (!isStandard(numStr)) {
            System.err.println("�����������ʽ����"); // �����ʽ�Ƿ�Ϸ�
            return 0;
        }
        numberStack = new Stack<Long>(); // ��ʼ��ջ
        symbolStack = new Stack<Character>();
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < numStr.length(); i++) {
            char ch = numStr.charAt(i); // ��ȡһ���ַ�
            if (isNumber(ch)) { // �жϵ�ǰ�ַ��Ƿ�Ϊ����
                temp.append(ch); 
            } else { 
                String tempStr = temp.toString(); 
                if (!tempStr.isEmpty()) {
                    long num = Long.parseLong(tempStr); 
                    numberStack.push(num); // ������ѹջ
                    temp = new StringBuffer(); // �������ֻ���
                }
                // �ж�����������ȼ�
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
                            if(midtemp<0) {   //�жϼ�������Ƿ�Ϊ����
                                midtemp=0;
                                exp = false;
                            }
                            numberStack.push(midtemp);
                            break;
                        case '*':
                            numberStack.push(a * b);
                            break;
                        case '/':     //�жϳ����Ƿ�Ϸ�����������
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
                    symbolStack.push(new Character(ch)); // ������ջ
                    if (ch == ')') { // ȥ������
                        symbolStack.pop();
                        symbolStack.pop();
                    }
                }
            }
        }
        // ���ؼ�����
        if(exp) 
        	return numberStack.pop();
        else 
        	return 999999;   
    }

     //ȥ���ַ����е����пո�
    String removeStrSpace(String str) {
        return str != null ? str.replaceAll(" ", "") : "";
    }

   
    //����������ʽ�Ļ����Ϸ��ԣ����Ϸ���true������false
    private boolean isStandard(String numStr) {
        if (numStr == null || numStr.isEmpty()) // ���ʽ����Ϊ��
            return false;
        Stack<Character> stack = new Stack<Character>(); // �����������ţ�������������Ƿ�ƥ��
        boolean b = false; 
        for (int i = 0; i < numStr.length(); i++) {
            char n = numStr.charAt(i);
            // �ж��ַ��Ƿ�Ϸ�
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
                if (stack.isEmpty() || !"(".equals((char) stack.pop() + "")) // �����Ƿ�ƥ��
                    return false;
            }
            // ����Ƿ��ж��'='��
            if ("=".equals(n + "")) {
                if (b)
                    return false;
                b = true;
            }
        }
        // ���ܻ���ȱ�������ŵ����
        if (!stack.isEmpty())
            return false;
        // ���'='���Ƿ���ĩβ
        if (!("=".equals(numStr.charAt(numStr.length() - 1) + "")))
            return false;
        return true;
    }

   //�ж�����
    private boolean isNumber(char num) {
        if (num >= '0' && num <= '9')
            return true;
        return false;
    }

   //�Ƚ����ȼ�
    private boolean comparePri(char symbol) {
        if (symbolStack.empty()) { 
            return true;
        }
        char top = (char) symbolStack.peek(); // �鿴��ջ�����Ķ���
        if (top == '(') {
            return true;
        }
        switch (symbol) {
            case '(': // ���ȼ����
                return true;
            case '*': {
                if (top == '+' || top == '-') // ���ȼ���+��-��
                    return true;
                else
                    return false;
            }
            case '/': {
                if (top == '+' || top == '-') // ���ȼ���+��-��
                    return true;
                else
                    return false;
            }
            case '+':
                return false;
            case '-':
                return false;
            case ')': // ���ȼ����
                return false;
            case '=': // ������
                return false;
            default:
                break;
        }
        return true;
    }


    public static void main(String args[]) {

        int num;
        int writer=0;
        int[] amount = {3,4,5};//��Ų�����������
        char[] symbol = {'+','-','*','/'};//��������������
        System.out.println("Please input num:");
        Scanner N = new Scanner(System.in);
        num = N.nextInt();
        for(int i=0;i<num;i++)
        {
            int m = (int)(Math.random()*amount.length);
            int k = amount[m]; //�������ĸ���
            String len = new String();
            //System.out.println(amount[m]);
            try {
                //����ļ����ڣ���׷�����ݣ�����ļ������ڣ��򴴽��ļ�
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
                    case 3://����������
                        int a = (int)(Math.random()*100)+1;//������a,b,c
                        int b = (int)(Math.random()*100)+1;
                        int c = (int)(Math.random()*100)+1;
                        int x = (int)(Math.random()*symbol.length);//������x,y
                        int y = (int)(Math.random()*symbol.length);
                        int bracket = (int)(Math.random()*3);//����,���ڸ�ʽ��ֻ����������������ֻ�����������ŵ�λ��
                        if(bracket==0)//0��ʾû������
                        {
                            len=String.valueOf(a)+String.valueOf(symbol[x])+String.valueOf(b)+String.valueOf(symbol[y]+String.valueOf(c)+"= ");
                        }
                        else if(bracket==1)//1��ʾ��������벿
                        {
                            len="("+String.valueOf(a)+String.valueOf(symbol[x])+String.valueOf(b)+")"+String.valueOf(symbol[y]+String.valueOf(c)+"= ");
                        }
                        else//2��ʾ�������Ұ벿
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
                        int bracket1 = (int)(Math.random()*4);//����,ע���ĸ�ʽ��������Ϊ2������ʱ��3�����
                        if(bracket1==0)//0��ʾû������
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
                        int bracket2 = (int)(Math.random()*9);//����,������4���������ʽ�ӣ�����ֻ�����˼�2�����ŵ�����8�����
                        if(bracket2==0)//������
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
