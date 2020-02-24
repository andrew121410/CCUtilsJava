package CCUtils.Utils;

import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;

public class Clipboard {

    public static void set(StringSelection stringSelection, ClipboardOwner clipboardOwner) {
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, clipboardOwner);
    }
}
