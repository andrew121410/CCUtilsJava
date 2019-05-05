package CCUtils.Utils;

public class EcDc {

    public EcDc() {

    }

    public String AEncryption(String what, int number) {
        if (number == 0) {
            number = 9;
        }

        StringBuffer stringBuffer = new StringBuffer(what);

        for (int i = 0; i < stringBuffer.length(); i++) {
            int temp = 0;
            temp = (int) stringBuffer.charAt(i);
            temp = temp * number;
            stringBuffer.setCharAt(i, (char) temp);
        }

        return stringBuffer.toString();

    }

    public String ADecryption(String what, int number) {
        if (number == 0) {
            number = 9;
        }

        StringBuffer stringBuffer = new StringBuffer(what);

        for (int i = 0; i < stringBuffer.length(); i++) {
            int temp = 0;
            temp = (int) stringBuffer.charAt(i);
            temp = temp / number;
            stringBuffer.setCharAt(i, (char) temp);
        }

        return stringBuffer.toString();

    }
}
