package CCUtils.Utils.encryption;

public class EcDc {

    public EcDc() {
    }

    public String AEncryption(String what, int number) {
        if (number == 0) {
            number = 9;
        }

        StringBuilder stringBuilder = new StringBuilder(what);

        for (int i = 0; i < stringBuilder.length(); i++) {
            int temp;
            temp = stringBuilder.charAt(i);
            temp = temp * number;
            stringBuilder.setCharAt(i, (char) temp);
        }

        return stringBuilder.toString();

    }

    public String ADecryption(String what, int number) {
        if (number == 0) {
            number = 9;
        }

        StringBuilder stringBuilder = new StringBuilder(what);

        for (int i = 0; i < stringBuilder.length(); i++) {
            int temp;
            temp = stringBuilder.charAt(i);
            temp = temp / number;
            stringBuilder.setCharAt(i, (char) temp);
        }

        return stringBuilder.toString();

    }
}
