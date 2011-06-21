package net.brainage.rfc.ui;

import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.SvnResource;
import net.brainage.rfc.model.CheckoutModel;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.ui.executor.CheckoutAsyncExecutor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.beans.PojoObservables;

public class CheckoutDialog extends Dialog
{

    private static final Logger log = LoggerFactory.getLogger(CheckoutDialog.class);

    DataBindingContext m_bindingContext;

    private CheckoutModel model = new CheckoutModel();

    protected Object result;
    protected Shell shell;
    private Table resourcesTable;
    private TableViewer resourcesTableViewer;
    private TableColumn actionColumn;
    private TableColumn pathColumn;
    private TableColumn mimeColumn;
    private Label opLabel;
    private Composite buttonPanel;
    private Button okButton;
    private Button cancelButton;

    private WorkPhaseContext phaseContext;
    
    private AsyncExecutor asyncExecutor;


    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public CheckoutDialog(Shell parent, int style, WorkPhaseContext phaseContext) {
        super(parent, style);
        setText("SWT Dialog");
        this.phaseContext = phaseContext;
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public Object open() {
        createContents();
        setValues();
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

    private void setValues() {
        ChangeRequest cr = this.phaseContext.getChangeRequest();
        String shellText = cr.getComponent() + " - Checkout";
        this.shell.setText(shellText);
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        this.shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        this.shell.addShellListener(new ShellAdapter() {
            public void shellActivated(ShellEvent e) {
                if (log.isInfoEnabled()) {
                    log.info("Checkout Dialog Activated...");
                }
                final Display display = Display.getCurrent();
                asyncExecutor = new CheckoutAsyncExecutor(phaseContext, model);
                new Thread("CheckoutAsyncExecutor") {
                	public void run() {
                		display.asyncExec(asyncExecutor);
                	}
                }.start();
                
            }
        });
        this.shell.setImage(SWTResourceManager.getImage(CheckoutDialog.class,
                "/icons/update.gif"));
        this.shell.setSize(630, 320);
        this.shell.setText("Checkout");
        this.shell.setLayout(new GridLayout(1, false));
        {
            resourcesTableViewer = new TableViewer(this.shell, SWT.BORDER | SWT.FULL_SELECTION);
            this.resourcesTable = resourcesTableViewer.getTable();
            this.resourcesTable.setLinesVisible(true);
            this.resourcesTable.setHeaderVisible(true);
            this.resourcesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            {
                this.actionColumn = new TableColumn(this.resourcesTable, SWT.NONE);
                this.actionColumn.setWidth(80);
                this.actionColumn.setText("Action");
            }
            {
                this.pathColumn = new TableColumn(this.resourcesTable, SWT.NONE);
                this.pathColumn.setWidth(540);
                this.pathColumn.setText("Path");
            }
            {
                this.mimeColumn = new TableColumn(this.resourcesTable, SWT.NONE);
                this.mimeColumn.setWidth(100);
                this.mimeColumn.setText("Mime type");
            }
        }
        {
            this.opLabel = new Label(this.shell, SWT.NONE);
            this.opLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            this.opLabel.setText("New Label");
        }
        {
            this.buttonPanel = new Composite(this.shell, SWT.NONE);
            this.buttonPanel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            this.buttonPanel.setLayout(new GridLayout(2, false));
            {
                this.okButton = new Button(this.buttonPanel, SWT.NONE);
                this.okButton.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        shell.close();
                    }
                });
                {
                    GridData gd_okButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
                    gd_okButton.widthHint = 80;
                    this.okButton.setLayoutData(gd_okButton);
                }
                this.okButton.setText("OK");
            }
            {
                this.cancelButton = new Button(this.buttonPanel, SWT.NONE);
                this.cancelButton.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                    	Display display = Display.getCurrent();
                    	display.disposeExec(asyncExecutor);
                    }
                });
                {
                    GridData gd_cancelButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
                            1);
                    gd_cancelButton.widthHint = 80;
                    this.cancelButton.setLayoutData(gd_cancelButton);
                }
                this.cancelButton.setText("Cancel");
            }
        }
        m_bindingContext = initDataBindings();

        if (log.isInfoEnabled()) {
            log.info("Checkout Dialog Contents Created.");
        }
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
        resourcesTableViewer.setContentProvider(listContentProvider);
        //
        IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), SvnResource.class, new String[]{"action", "path", "mimeType"});
        resourcesTableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
        //
        IObservableList modelResourcesObserveList = BeansObservables.observeList(Realm.getDefault(), model, "resources");
        resourcesTableViewer.setInput(modelResourcesObserveList);
        //
        IObservableValue modelTopIndexObserveValue = BeansObservables.observeValue(model, "topIndex");
        IObservableValue resourcesTableTopIndexObserveValue = PojoObservables.observeValue(resourcesTable, "topIndex");
        bindingContext.bindValue(modelTopIndexObserveValue, resourcesTableTopIndexObserveValue, null, null);
        //
        IObservableValue modelStatusObserveValue = BeansObservables.observeValue(model, "status");
        IObservableValue opLabelTextObserveValue = PojoObservables.observeValue(opLabel, "text");
        bindingContext.bindValue(modelStatusObserveValue, opLabelTextObserveValue, null, null);
        //
        return bindingContext;
    }
}
