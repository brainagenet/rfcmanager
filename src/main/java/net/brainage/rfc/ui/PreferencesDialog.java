/*
 * net.brainage.rfc.ui.PreferencesDialog.java
 * Created on 2011. 6. 19.
 */
package net.brainage.rfc.ui;

import net.brainage.rfc.config.Configuration;
import net.brainage.rfc.config.Configuration.Key;
import net.brainage.rfc.util.StringUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class PreferencesDialog extends Dialog
{

    private static final Logger log = LoggerFactory.getLogger(PreferencesDialog.class);

    public static final String[] SUPPORT_PROTOCOLS = new String[] {
            "http://", "https://", "svn://", "svn+ssh:///", "file://" };

    private Configuration config = Configuration.getInstance();

    protected Object result;
    protected Shell shell;
    private Group svnGroup;
    private Label svnProtocolLabel;
    private Combo svnProtocolCombo;
    private Label svnHostLabel;
    private Text svnHostText;
    private Label svnPortLabel;
    private Text svnPortText;
    private Group authGroup;
    private Label usernameLabel;
    private Text usernameText;
    private Label passwordLabel;
    private Text passwordText;
    private Group workspaceGroup;
    private Label wcDirLabel;
    private Text wcDirText;
    private Button wcDirBrowseButton;
    private Label tmpDirLabel;
    private Text tmpDirText;
    private Button tmpDirBrowseButton;
    private Composite buttonPanel;
    private Button cancelButton;
    private Button okButton;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public PreferencesDialog(Shell parent, int style) {
        super(parent, style);
        // setText("SWT Dialog");
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public Object open() {
        createContents();
        setConfiguration();
        this.shell.open();
        this.shell.layout();
        Display display = getParent().getDisplay();
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return this.result;
    }

    private void setConfiguration() {
        if (log.isInfoEnabled()) {
            log.info("set values of UI Component from Configuration...");
        }

        /* ---------------------------------------------------- */
        /* subversion */
        /* ---------------------------------------------------- */
        int protocolSelect = config.getInt(Configuration.Key.SVN_PROTOCOL_SELECT);
        svnProtocolCombo.select(protocolSelect);

        String host = config.getString(Configuration.Key.SVN_HOST);
        if (StringUtils.hasText(host)) {
            this.svnHostText.setText(host);
        }

        String port = config.getString(Configuration.Key.SVN_PORT);
        if (StringUtils.hasText(port)) {
            this.svnPortText.setText(port);
        }

        /* ---------------------------------------------------- */
        /* authentication */
        /* ---------------------------------------------------- */
        String username = config.getString(Configuration.Key.SVN_AUTH_USERNAME);
        if (StringUtils.hasText(username)) {
            this.usernameText.setText(username);
        }

        String password = config.getString(Configuration.Key.SVN_AUTH_PASSWORD);
        if (StringUtils.hasText(password)) {
            this.passwordText.setText(password);
        }

        /* ---------------------------------------------------- */
        /* workspaces */
        /* ---------------------------------------------------- */
        String wcdir = config.getString(Configuration.Key.WORKSPACE_WC);
        if (StringUtils.hasText(wcdir)) {
            this.wcDirText.setText(wcdir);
        }

        String tmpdir = config.getString(Configuration.Key.WORKSPACE_TMP);
        if (StringUtils.hasText(tmpdir)) {
            this.tmpDirText.setText(tmpdir);
        }
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        this.shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        this.shell.setImage(SWTResourceManager.getImage(PreferencesDialog.class,
                "/icons/config_obj.gif"));
        this.shell.setSize(380, 390);
        this.shell.setText("Preferences");
        this.shell.setLayout(new GridLayout(1, false));

        this.svnGroup = new Group(this.shell, SWT.NONE);
        this.svnGroup.setLayout(new GridLayout(2, false));
        this.svnGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.svnGroup.setText("Subversion");

        this.svnProtocolLabel = new Label(this.svnGroup, SWT.NONE);
        GridData gd_svnProtocolLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_svnProtocolLabel.widthHint = 70;
        this.svnProtocolLabel.setLayoutData(gd_svnProtocolLabel);
        this.svnProtocolLabel.setText("Protocol :");

        this.svnProtocolCombo = new Combo(this.svnGroup, SWT.NONE);
        this.svnProtocolCombo.setItems(SUPPORT_PROTOCOLS);
        GridData gd_svnProtocolCombo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_svnProtocolCombo.widthHint = 80;
        this.svnProtocolCombo.setLayoutData(gd_svnProtocolCombo);
        this.svnProtocolCombo.select(2);

        this.svnHostLabel = new Label(this.svnGroup, SWT.NONE);
        GridData gd_svnHostLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_svnHostLabel.widthHint = 70;
        this.svnHostLabel.setLayoutData(gd_svnHostLabel);
        this.svnHostLabel.setText("Host :");

        this.svnHostText = new Text(this.svnGroup, SWT.BORDER);
        this.svnHostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.svnPortLabel = new Label(this.svnGroup, SWT.NONE);
        GridData gd_svnPortLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_svnPortLabel.widthHint = 70;
        this.svnPortLabel.setLayoutData(gd_svnPortLabel);
        this.svnPortLabel.setText("Port :");

        this.svnPortText = new Text(this.svnGroup, SWT.BORDER);
        this.svnPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.authGroup = new Group(this.shell, SWT.NONE);
        this.authGroup.setLayout(new GridLayout(2, false));
        this.authGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.authGroup.setText("Authentication");

        this.usernameLabel = new Label(this.authGroup, SWT.NONE);
        GridData gd_usernameLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_usernameLabel.widthHint = 70;
        this.usernameLabel.setLayoutData(gd_usernameLabel);
        this.usernameLabel.setText("Username :");

        this.usernameText = new Text(this.authGroup, SWT.BORDER);
        this.usernameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.passwordLabel = new Label(this.authGroup, SWT.NONE);
        GridData gd_passwordLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_passwordLabel.widthHint = 70;
        this.passwordLabel.setLayoutData(gd_passwordLabel);
        this.passwordLabel.setText("Password :");

        this.passwordText = new Text(this.authGroup, SWT.BORDER | SWT.PASSWORD);
        this.passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.workspaceGroup = new Group(this.shell, SWT.NONE);
        this.workspaceGroup.setLayout(new GridLayout(2, false));
        this.workspaceGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.workspaceGroup.setText("Workspace");

        this.wcDirLabel = new Label(this.workspaceGroup, SWT.NONE);
        this.wcDirLabel.setText("Working Copy :");
        new Label(this.workspaceGroup, SWT.NONE);

        this.wcDirText = new Text(this.workspaceGroup, SWT.BORDER);
        this.wcDirText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.wcDirBrowseButton = new Button(this.workspaceGroup, SWT.NONE);
        this.wcDirBrowseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(shell);
                dialog.setFilterPath(wcDirText.getText());
                dialog.setText("Browse...");
                dialog.setMessage("Select subversion working copy directory...");
                String dir = dialog.open();
                if (StringUtils.hasText(dir)) {
                    wcDirText.setText(dir);
                }
            }
        });
        GridData gd_wcDirBrowseButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_wcDirBrowseButton.widthHint = 70;
        this.wcDirBrowseButton.setLayoutData(gd_wcDirBrowseButton);
        this.wcDirBrowseButton.setText("Browse...");

        this.tmpDirLabel = new Label(this.workspaceGroup, SWT.NONE);
        this.tmpDirLabel.setText("Temporary :");
        new Label(this.workspaceGroup, SWT.NONE);

        this.tmpDirText = new Text(this.workspaceGroup, SWT.BORDER);
        this.tmpDirText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.tmpDirBrowseButton = new Button(this.workspaceGroup, SWT.NONE);
        this.tmpDirBrowseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(shell);
                dialog.setFilterPath(wcDirText.getText());
                dialog.setText("Browse...");
                dialog.setMessage("Select temporary directory...");
                String dir = dialog.open();
                if (StringUtils.hasText(dir)) {
                    tmpDirText.setText(dir);
                }
            }
        });
        GridData gd_tmpDirBrowseButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_tmpDirBrowseButton.widthHint = 70;
        this.tmpDirBrowseButton.setLayoutData(gd_tmpDirBrowseButton);
        this.tmpDirBrowseButton.setText("Browse...");

        this.buttonPanel = new Composite(this.shell, SWT.NONE);
        this.buttonPanel.setLayout(new GridLayout(2, false));
        this.buttonPanel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        this.okButton = new Button(this.buttonPanel, SWT.NONE);
        this.okButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                /* ---------------------------------------------------- */
                /* subversion */
                /* ---------------------------------------------------- */
                int selectedIndex = svnProtocolCombo.getSelectionIndex();
                String text = svnProtocolCombo.getText();
                if (selectedIndex > -1) {
                    config.setInt(Key.SVN_PROTOCOL_SELECT, selectedIndex);
                    config.setString(Key.SVN_PROTOCOL, text);
                }

                text = svnHostText.getText();
                if (StringUtils.hasText(text)) {
                    config.setString(Key.SVN_HOST, text);
                }

                text = svnPortText.getText();
                if (StringUtils.hasText(text)) {
                    config.setString(Key.SVN_PORT, text);
                }

                /* ---------------------------------------------------- */
                /* authentication */
                /* ---------------------------------------------------- */
                text = usernameText.getText();
                if (StringUtils.hasText(text)) {
                    config.setString(Key.SVN_AUTH_USERNAME, text);
                }

                text = passwordText.getText();
                if (StringUtils.hasText(text)) {
                    config.setString(Key.SVN_AUTH_PASSWORD, text);
                }

                /* ---------------------------------------------------- */
                /* workspaces */
                /* ---------------------------------------------------- */
                text = wcDirText.getText();
                if (StringUtils.hasText(text)) {
                    config.setString(Key.WORKSPACE_WC, text);
                }

                text = tmpDirText.getText();
                if (StringUtils.hasText(text)) {
                    config.setString(Key.WORKSPACE_TMP, text);
                }

                /* ---------------------------------------------------- */
                /* store configuration */
                /* ---------------------------------------------------- */
                config.store();

                /* ---------------------------------------------------- */
                /* close dialog */
                /* ---------------------------------------------------- */
                shell.close();
            }
        });
        GridData gd_okButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_okButton.widthHint = 70;
        this.okButton.setLayoutData(gd_okButton);
        this.okButton.setText("OK");

        this.cancelButton = new Button(this.buttonPanel, SWT.NONE);
        this.cancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
        GridData gd_cancelButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_cancelButton.widthHint = 70;
        this.cancelButton.setLayoutData(gd_cancelButton);
        this.cancelButton.setText("Cancel");
    }

}
