import view.LoginFrame;
import javax.swing.*;

public class BankingSystem {
    public static void main(String[] args) {
        // Use Nimbus LAF — it actually respects setBackground() on buttons.
        // The default Windows/System LAF ignores button colors (white-on-white bug).
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Nimbus not available — fall back to cross-platform Metal (still better than System)
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) {}
        }
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

