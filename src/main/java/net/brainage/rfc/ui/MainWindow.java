/*
 * net.brainage.rfc.ui.MainWindow.java
 * Created on 2011. 6. 19.
 */
package net.brainage.rfc.ui;

import java.beans.PropertyChangeListener;
import java.io.File;

import net.brainage.rfc.Constants;
import net.brainage.rfc.config.Configuration;
import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.ChangeRequestResource;
import net.brainage.rfc.model.ErrorDescription;
import net.brainage.rfc.model.ViewHolder;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.ui.event.ConnectionUrlChangeListener;
import net.brainage.rfc.ui.event.FilePropertyChangeListener;
import net.brainage.rfc.ui.executor.RunAsyncExecutor;
import net.brainage.rfc.util.svn.SvnClient;
import net.brainage.rfc.util.svn.SvnClientImpl;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

/**
 * 
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class MainWindow
{
    DataBindingContext m_bindingContext;

    private static final Logger log = LoggerFactory.getLogger(MainWindow.class);

    Configuration config = Configuration.getInstance();

    /* ---------- Models ---------- */
    private ChangeRequest changeRequest = new ChangeRequest();
    private WorkPhaseContext phaseContext = new WorkPhaseContext(changeRequest);

    /* ---------- UI Components ---------- */
    protected Shell shell;
    private ToolBar toolBar;
    private ToolItem openToolItem;
    private ToolItem runToolItem;
    private ToolItem prefToolItem;
    private ToolItem exitToolItem;
    private Label toolBarSeparator;
    private Composite contentPanel;
    private Label statusSepararot;
    private Group crformPanel;
    private Label componentLabel;
    private Label moduleLabel;
    private Label summaryLabel;
    private Text summaryText;
    private Group progressPanel;
    private Label progressNameLabel;
    private ProgressBar progressBar;
    private Label ProgressDescriptionLabel;
    private TabFolder tabFolder;
    private TabItem resourcesTabItem;
    private Composite resourcesPanel;
    private TabItem errorsTabItem;
    private Composite errorsPanel;
    private Label fileLabel;
    private Text fileText;
    private Table resourcesTable;
    private TableViewer resourcesTableViewer;
    private TableColumn resNoColumn;
    private TableColumn resourceColumn;
    private TableColumn revisionColumn;
    private TableColumn typeColumn;
    private TableColumn statusColumn;
    private Composite statusPanel;
    private Label statusLabel;
    private Label connectionLabel;
    private Label errorStatusLabel;
    private Table errorsTable;
    private TableViewer errorsTableViewer;
    private TableColumn errDescColumn;
    private TableColumn errNoColumn;
    private TableColumn errResourceColumn;
    private ToolItem checkoutToolItem;
    private Combo componentCombo;
    private Combo moduleCombo;

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        Display display = Display.getDefault();
        Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable()
        {
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.open();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        registerPropertyEvent();
        createContents();
        this.shell.open();
        this.shell.layout();
        while ( !this.shell.isDisposed() ) {
            if ( !display.readAndDispatch() ) {
                display.sleep();
            }
        }
    }

    /**
     * 
     */
    private void registerPropertyEvent() {
        this.changeRequest.addPropertyChangeListener("file", new FilePropertyChangeListener(
                changeRequest));
        PropertyChangeListener listener = new ConnectionUrlChangeListener(changeRequest);
        this.changeRequest.addPropertyChangeListener("component", listener);
        this.changeRequest.addPropertyChangeListener("module", listener);
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        this.shell = new Shell();
        this.shell
                .setImage(SWTResourceManager.getImage(MainWindow.class, "/icons/silk/bricks.png"));
        this.shell.setSize(900, 700);
        this.shell.setText("Change Request Manager - Subversion");
        GridLayout gl_shell = new GridLayout(1, false);
        gl_shell.verticalSpacing = 0;
        gl_shell.marginWidth = 0;
        gl_shell.marginHeight = 0;
        gl_shell.horizontalSpacing = 0;
        this.shell.setLayout(gl_shell);

        createToolBarPanel();

        this.toolBarSeparator = new Label(this.shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_toolBarSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_toolBarSeparator.verticalIndent = 5;
        this.toolBarSeparator.setLayoutData(gd_toolBarSeparator);

        this.contentPanel = new Composite(this.shell, SWT.NONE);
        this.contentPanel.setLayout(new GridLayout(1, false));
        this.contentPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

        this.crformPanel = new Group(this.contentPanel, SWT.NONE);
        this.crformPanel.setText("Change Request Form");
        this.crformPanel.setLayout(new GridLayout(4, false));
        this.crformPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.componentLabel = new Label(this.crformPanel, SWT.NONE);
        this.componentLabel.setAlignment(SWT.RIGHT);
        GridData gd_componentLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_componentLabel.widthHint = 70;
        this.componentLabel.setLayoutData(gd_componentLabel);
        this.componentLabel.setText("Component :");

        this.componentCombo = new Combo(this.crformPanel, SWT.NONE);
        this.componentCombo.setItems(Constants.COMPONENT_LIST);
        this.componentCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.moduleLabel = new Label(this.crformPanel, SWT.NONE);
        this.moduleLabel.setAlignment(SWT.RIGHT);
        GridData gd_moduleLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_moduleLabel.widthHint = 70;
        this.moduleLabel.setLayoutData(gd_moduleLabel);
        this.moduleLabel.setText("Module :");

        this.moduleCombo = new Combo(this.crformPanel, SWT.NONE);
        this.moduleCombo.setItems(Constants.MODULE_LIST);
        this.moduleCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.summaryLabel = new Label(this.crformPanel, SWT.NONE);
        GridData gd_summaryLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_summaryLabel.widthHint = 70;
        this.summaryLabel.setLayoutData(gd_summaryLabel);
        this.summaryLabel.setAlignment(SWT.RIGHT);
        this.summaryLabel.setText("Summary :");

        this.summaryText = new Text(this.crformPanel, SWT.BORDER | SWT.WRAP | SWT.MULTI);
        GridData gd_summaryText = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        gd_summaryText.heightHint = 75;
        this.summaryText.setLayoutData(gd_summaryText);

        this.progressPanel = new Group(this.contentPanel, SWT.NONE);
        this.progressPanel.setLayout(new GridLayout(1, false));
        this.progressPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.progressPanel.setText("Progress");

        this.progressNameLabel = new Label(this.progressPanel, SWT.NONE);
        this.progressNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.progressNameLabel.setText("Progress Name");

        this.progressBar = new ProgressBar(this.progressPanel, SWT.NONE);
        this.progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        this.ProgressDescriptionLabel = new Label(this.progressPanel, SWT.NONE);
        GridData gd_ProgressDescriptionLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
                1);
        gd_ProgressDescriptionLabel.horizontalIndent = 10;
        this.ProgressDescriptionLabel.setLayoutData(gd_ProgressDescriptionLabel);
        this.ProgressDescriptionLabel.setText("Progress Description");

        this.tabFolder = new TabFolder(this.contentPanel, SWT.NONE);
        this.tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        this.resourcesTabItem = new TabItem(this.tabFolder, SWT.NONE);
        this.resourcesTabItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/resource_hist.gif"));
        this.resourcesTabItem.setText("Resources");

        this.resourcesPanel = new Composite(this.tabFolder, SWT.NONE);
        this.resourcesTabItem.setControl(this.resourcesPanel);
        this.resourcesPanel.setLayout(new GridLayout(2, false));

        this.fileLabel = new Label(this.resourcesPanel, SWT.NONE);
        GridData gd_fileLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_fileLabel.horizontalIndent = 5;
        gd_fileLabel.widthHint = 40;
        this.fileLabel.setLayoutData(gd_fileLabel);
        this.fileLabel.setText("File :");

        this.fileText = new Text(this.resourcesPanel, SWT.BORDER);
        this.fileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        resourcesTableViewer = new TableViewer(this.resourcesPanel, SWT.BORDER | SWT.FULL_SELECTION);
        this.resourcesTable = resourcesTableViewer.getTable();
        this.resourcesTable.setLinesVisible(true);
        this.resourcesTable.setHeaderVisible(true);
        this.resourcesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        this.resNoColumn = new TableColumn(this.resourcesTable, SWT.NONE);
        this.resNoColumn.setWidth(50);
        this.resNoColumn.setText("No");

        this.resourceColumn = new TableColumn(this.resourcesTable, SWT.NONE);
        this.resourceColumn.setWidth(450);
        this.resourceColumn.setText("Resource");

        this.revisionColumn = new TableColumn(this.resourcesTable, SWT.NONE);
        this.revisionColumn.setWidth(70);
        this.revisionColumn.setText("Revision");

        this.typeColumn = new TableColumn(this.resourcesTable, SWT.NONE);
        this.typeColumn.setWidth(70);
        this.typeColumn.setText("Type");

        this.statusColumn = new TableColumn(this.resourcesTable, SWT.NONE);
        this.statusColumn.setWidth(100);
        this.statusColumn.setText("Status");

        this.errorsTabItem = new TabItem(this.tabFolder, SWT.NONE);
        this.errorsTabItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/error.gif"));
        this.errorsTabItem.setText("Errors");

        this.errorsPanel = new Composite(this.tabFolder, SWT.NONE);
        this.errorsTabItem.setControl(this.errorsPanel);
        this.errorsPanel.setLayout(new GridLayout(1, false));

        this.errorStatusLabel = new Label(this.errorsPanel, SWT.NONE);
        this.errorStatusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.errorStatusLabel.setText("0 errors");

        errorsTableViewer = new TableViewer(this.errorsPanel, SWT.BORDER | SWT.FULL_SELECTION);
        this.errorsTable = errorsTableViewer.getTable();
        this.errorsTable.setLinesVisible(true);
        this.errorsTable.setHeaderVisible(true);
        this.errorsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        this.errNoColumn = new TableColumn(this.errorsTable, SWT.NONE);
        this.errNoColumn.setWidth(50);
        this.errNoColumn.setText("No");

        this.errDescColumn = new TableColumn(this.errorsTable, SWT.NONE);
        this.errDescColumn.setWidth(450);
        this.errDescColumn.setText("Description");

        this.errResourceColumn = new TableColumn(this.errorsTable, SWT.NONE);
        this.errResourceColumn.setWidth(200);
        this.errResourceColumn.setText("Resource");

        this.statusSepararot = new Label(this.shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        this.statusSepararot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        this.statusPanel = new Composite(this.shell, SWT.NONE);
        this.statusPanel.setLayout(new GridLayout(2, false));
        this.statusPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        this.statusLabel = new Label(this.statusPanel, SWT.NONE);
        this.statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.connectionLabel = new Label(this.statusPanel, SWT.NONE);
        this.connectionLabel.setAlignment(SWT.RIGHT);
        this.connectionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_bindingContext = initDataBindings();

    }

    private void createToolBarPanel() {
        this.toolBar = new ToolBar(this.shell, SWT.FLAT | SWT.RIGHT);
        GridData gd_toolBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_toolBar.verticalIndent = 5;
        gd_toolBar.horizontalIndent = 5;
        this.toolBar.setLayoutData(gd_toolBar);

        this.openToolItem = new ToolItem(this.toolBar, SWT.NONE);
        this.openToolItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);
                dialog.setFilterNames(new String[] { "Microsoft Excel Spreadsheet Files (*.xls)" });
                dialog.setFilterExtensions(new String[] { "*.xls" });
                String selectedFilename = dialog.open();
                if ( selectedFilename != null ) {
                    changeRequest.setFile(selectedFilename);
                    runToolItem.setEnabled(true);
                    checkoutToolItem.setEnabled(true);
                }
            }
        });
        this.openToolItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/fldr_obj.gif"));
        this.openToolItem.setText("&Open");

        this.checkoutToolItem = new ToolItem(this.toolBar, SWT.NONE);
        this.checkoutToolItem.setEnabled(false);
        this.checkoutToolItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e) {
                if ( componentCombo.getSelectionIndex() == -1 ) {
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING);
                    messageBox.setText("Warning");
                    messageBox.setMessage("Please select component");
                    messageBox.open();
                    return;
                }

                if ( moduleCombo.getSelectionIndex() == -1 ) {
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING);
                    messageBox.setText("Warning");
                    messageBox.setMessage("Please select module for " + componentCombo.getText());
                    messageBox.open();
                    return;
                }

                boolean flag = false;
                String wcdir = config.getString(Configuration.Key.WORKSPACE_WC);
                StringBuffer buf = new StringBuffer(wcdir);
                buf.append("/").append(changeRequest.getComponent());
                buf.append("/build/").append(changeRequest.getModule());

                File buildDirectory = new File(buf.toString());
                if ( buildDirectory.exists() == false ) {
                    buildDirectory.mkdirs();
                    flag = true;
                } else {
                    SvnClient svnClient = SvnClientImpl.getClient();
                    try {
                        flag = !svnClient.isWorkingCopyRoot(buildDirectory);
                    } catch ( SVNException e1 ) {
                        e1.printStackTrace();
                    }
                }

                if ( flag ) {
                    CheckoutDialog dialog = new CheckoutDialog(shell, SWT.NONE, phaseContext);
                    dialog.open();
                } else {
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
                    messageBox.setText("Checkout Information");
                    messageBox.setMessage("이미 Checkout 했습니다.");
                    messageBox.open();
                }
            }
        });
        this.checkoutToolItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/update.gif"));
        this.checkoutToolItem.setText("&Checkout");

        this.runToolItem = new ToolItem(this.toolBar, SWT.NONE);
        this.runToolItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e) {
                if ( log.isInfoEnabled() ) {
                    log.info("initialize Work Phase Context...");
                }
                phaseContext.initialize();
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.setDisplay(Display.getCurrent());
                viewHolder.setShell(shell);
                phaseContext.setViewHolder(viewHolder);
                new Thread(new RunAsyncExecutor(phaseContext)).start();
            }
        });
        this.runToolItem.setEnabled(false);
        this.runToolItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/start_task.gif"));
        this.runToolItem.setText("&Run");

        new ToolItem(this.toolBar, SWT.SEPARATOR);

        this.prefToolItem = new ToolItem(this.toolBar, SWT.NONE);
        this.prefToolItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e) {
                PreferencesDialog dialog = new PreferencesDialog(shell, SWT.OPEN);
                dialog.open();
            }
        });
        this.prefToolItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/config_obj.gif"));
        this.prefToolItem.setText("&Preferences");

        new ToolItem(this.toolBar, SWT.SEPARATOR);

        this.exitToolItem = new ToolItem(this.toolBar, SWT.NONE);
        this.exitToolItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
        this.exitToolItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/rem_co.gif"));
        this.exitToolItem.setText("E&xit");
    }

    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue changeRequestFileObserveValue = BeansObservables.observeValue(
                changeRequest, "file");
        IObservableValue fileTextObserveTextObserveWidget = SWTObservables.observeText(fileText,
                SWT.Modify);
        bindingContext.bindValue(changeRequestFileObserveValue, fileTextObserveTextObserveWidget,
                null, null);
        //
        IObservableValue changeRequestSummaryObserveValue = BeansObservables.observeValue(
                changeRequest, "summary");
        IObservableValue summaryTextObserveTextObserveWidget = SWTObservables.observeText(
                summaryText, SWT.Modify);
        bindingContext.bindValue(changeRequestSummaryObserveValue,
                summaryTextObserveTextObserveWidget, null, null);
        //
        ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
        resourcesTableViewer.setContentProvider(listContentProvider);
        //
        IObservableMap[] observeMaps = BeansObservables.observeMaps(
                listContentProvider.getKnownElements(), ChangeRequestResource.class, new String[] {
                        "no", "resource", "revision", "type", "status" });
        resourcesTableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
        //
        IObservableList changeRequestResourcesObserveList = BeansObservables.observeList(
                Realm.getDefault(), changeRequest, "resources");
        resourcesTableViewer.setInput(changeRequestResourcesObserveList);
        //
        IObservableValue phaseContextPhaseNameObserveValue = BeansObservables.observeValue(
                phaseContext, "phaseName");
        IObservableValue progressNameLabelObserveTextObserveWidget = SWTObservables
                .observeText(progressNameLabel);
        bindingContext.bindValue(phaseContextPhaseNameObserveValue,
                progressNameLabelObserveTextObserveWidget, null, null);
        //
        IObservableValue phaseContextPhaseDescriptionObserveValue = BeansObservables.observeValue(
                phaseContext, "phaseDescription");
        IObservableValue progressDescriptionLabelObserveTextObserveWidget = SWTObservables
                .observeText(ProgressDescriptionLabel);
        bindingContext.bindValue(phaseContextPhaseDescriptionObserveValue,
                progressDescriptionLabelObserveTextObserveWidget, null, null);
        //
        ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
        errorsTableViewer.setContentProvider(listContentProvider_1);
        //
        IObservableMap[] observeMaps_1 = BeansObservables.observeMaps(
                listContentProvider_1.getKnownElements(), ErrorDescription.class, new String[] {
                        "no", "description", "resource" });
        errorsTableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps_1));
        //
        IObservableList phaseContextErrorsObserveList = BeansObservables.observeList(
                Realm.getDefault(), phaseContext, "errors");
        errorsTableViewer.setInput(phaseContextErrorsObserveList);
        //
        IObservableValue changeRequestConnectionUrlObserveValue = BeansObservables.observeValue(
                changeRequest, "connectionUrl");
        IObservableValue connectionLabelObserveTextObserveWidget = SWTObservables
                .observeText(connectionLabel);
        bindingContext.bindValue(changeRequestConnectionUrlObserveValue,
                connectionLabelObserveTextObserveWidget, null, null);
        //
        IObservableValue changeRequestModuleSelectObserveValue = BeansObservables.observeValue(
                changeRequest, "moduleSelect");
        IObservableValue moduleComboObserveSingleSelectionIndexObserveWidget_1 = SWTObservables
                .observeSingleSelectionIndex(moduleCombo);
        bindingContext.bindValue(changeRequestModuleSelectObserveValue,
                moduleComboObserveSingleSelectionIndexObserveWidget_1, null, null);
        //
        IObservableValue changeRequestComponentSelectObserveValue = BeansObservables.observeValue(
                changeRequest, "componentSelect");
        IObservableValue componentComboObserveSingleSelectionIndexObserveWidget = SWTObservables
                .observeSingleSelectionIndex(componentCombo);
        bindingContext.bindValue(changeRequestComponentSelectObserveValue,
                componentComboObserveSingleSelectionIndexObserveWidget, null, null);
        //
        IObservableValue moduleComboObserveTextObserveWidget = SWTObservables
                .observeText(moduleCombo);
        IObservableValue changeRequestModuleObserveValue = BeansObservables.observeValue(
                changeRequest, "module");
        bindingContext.bindValue(moduleComboObserveTextObserveWidget,
                changeRequestModuleObserveValue, null, null);
        //
        IObservableValue componentComboObserveTextObserveWidget = SWTObservables
                .observeText(componentCombo);
        IObservableValue changeRequestComponentObserveValue = BeansObservables.observeValue(
                changeRequest, "component");
        bindingContext.bindValue(componentComboObserveTextObserveWidget,
                changeRequestComponentObserveValue, null, null);
        //
        return bindingContext;
    }
}
