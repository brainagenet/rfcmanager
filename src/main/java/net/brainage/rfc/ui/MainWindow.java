/*
 * net.brainage.rfc.ui.MainWindow.java
 * Created on 2011. 6. 19.
 */
package net.brainage.rfc.ui;

import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.ChangeRequestResource;
import net.brainage.rfc.model.ErrorDescription;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.ui.event.FilePropertyChangeListener;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
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
    private Text componentText;
    private Text moduleText;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        Display display = Display.getDefault();
        Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.open();
                } catch (Exception e) {
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
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
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
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        this.shell = new Shell();
        this.shell.setImage(SWTResourceManager.getImage(MainWindow.class, "/icons/silk/bricks.png"));
        this.shell.setSize(800, 600);
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
        
        this.componentText = new Text(this.crformPanel, SWT.BORDER);
        this.componentText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        this.moduleLabel = new Label(this.crformPanel, SWT.NONE);
        this.moduleLabel.setAlignment(SWT.RIGHT);
        GridData gd_moduleLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_moduleLabel.widthHint = 70;
        this.moduleLabel.setLayoutData(gd_moduleLabel);
        this.moduleLabel.setText("Module :");
        
        this.moduleText = new Text(this.crformPanel, SWT.BORDER);
        this.moduleText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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
        this.openToolItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);
                dialog.setFilterNames(new String[] { "Microsoft Excel Spreadsheet Files (*.xls)" });
                dialog.setFilterExtensions(new String[] { "*.xls" });
                String selectedFilename = dialog.open();
                if (selectedFilename != null) {
                    changeRequest.setFile(selectedFilename);
                    runToolItem.setEnabled(true);
                }
            }
        });
        this.openToolItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/fldr_obj.gif"));
        this.openToolItem.setText("&Open");

        this.runToolItem = new ToolItem(this.toolBar, SWT.NONE);
        this.runToolItem.setEnabled(false);
        this.runToolItem.setImage(SWTResourceManager.getImage(MainWindow.class,
                "/icons/start_task.gif"));
        this.runToolItem.setText("&Run");

        new ToolItem(this.toolBar, SWT.SEPARATOR);

        this.prefToolItem = new ToolItem(this.toolBar, SWT.NONE);
        this.prefToolItem.addSelectionListener(new SelectionAdapter() {
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
        this.exitToolItem.addSelectionListener(new SelectionAdapter() {
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
        IObservableValue changeRequestFileObserveValue = BeansObservables.observeValue(changeRequest, "file");
        IObservableValue fileTextObserveTextObserveWidget = SWTObservables.observeText(fileText, SWT.Modify);
        bindingContext.bindValue(changeRequestFileObserveValue, fileTextObserveTextObserveWidget, null, null);
        //
        IObservableValue changeRequestComponentObserveValue = BeansObservables.observeValue(changeRequest, "component");
        IObservableValue componentTextObserveTextObserveWidget = SWTObservables.observeText(componentText, SWT.Modify);
        bindingContext.bindValue(changeRequestComponentObserveValue, componentTextObserveTextObserveWidget, null, null);
        //
        IObservableValue changeRequestModuleObserveValue = BeansObservables.observeValue(changeRequest, "module");
        IObservableValue moduleTextObserveTextObserveWidget = SWTObservables.observeText(moduleText, SWT.Modify);
        bindingContext.bindValue(changeRequestModuleObserveValue, moduleTextObserveTextObserveWidget, null, null);
        //
        IObservableValue changeRequestSummaryObserveValue = BeansObservables.observeValue(changeRequest, "summary");
        IObservableValue summaryTextObserveTextObserveWidget = SWTObservables.observeText(summaryText, SWT.Modify);
        bindingContext.bindValue(changeRequestSummaryObserveValue, summaryTextObserveTextObserveWidget, null, null);
        //
        ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
        resourcesTableViewer.setContentProvider(listContentProvider);
        //
        IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), ChangeRequestResource.class, new String[]{"no", "resource", "revision", "type", "status"});
        resourcesTableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
        //
        IObservableList changeRequestResourcesObserveList = BeansObservables.observeList(Realm.getDefault(), changeRequest, "resources");
        resourcesTableViewer.setInput(changeRequestResourcesObserveList);
        //
        IObservableValue phaseContextPhaseNameObserveValue = BeansObservables.observeValue(phaseContext, "phaseName");
        IObservableValue progressNameLabelObserveTextObserveWidget = SWTObservables.observeText(progressNameLabel);
        bindingContext.bindValue(phaseContextPhaseNameObserveValue, progressNameLabelObserveTextObserveWidget, null, null);
        //
        IObservableValue phaseContextPhaseDescriptionObserveValue = BeansObservables.observeValue(phaseContext, "phaseDescription");
        IObservableValue progressDescriptionLabelObserveTextObserveWidget = SWTObservables.observeText(ProgressDescriptionLabel);
        bindingContext.bindValue(phaseContextPhaseDescriptionObserveValue, progressDescriptionLabelObserveTextObserveWidget, null, null);
        //
        ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
        errorsTableViewer.setContentProvider(listContentProvider_1);
        //
        IObservableMap[] observeMaps_1 = BeansObservables.observeMaps(listContentProvider_1.getKnownElements(), ErrorDescription.class, new String[]{"no", "description", "resource"});
        errorsTableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps_1));
        //
        IObservableList phaseContextErrorsObserveList = BeansObservables.observeList(Realm.getDefault(), phaseContext, "errors");
        errorsTableViewer.setInput(phaseContextErrorsObserveList);
        //
        return bindingContext;
    }
}
