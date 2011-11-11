#include "mainwindow.h"
#include "new_account.h"


MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
{
    resize(WinWidth, WinHeight);

    newAccountAction = new QAction(tr("&New Account..."), this);
    newAccountAction->setStatusTip(tr("New Account..."));
    newAccountAction->setShortcut(tr("F1"));
    connect(newAccountAction, SIGNAL(triggered()), this, SLOT(newAccount()));

    findUserAction = new QAction(tr("&Find User..."), this);
    findUserAction->setStatusTip(tr("Find User..."));
    findUserAction->setShortcut(tr("F3"));
    connect(findUserAction, SIGNAL(triggered()), this, SLOT(findUserAccount()));

    transactionAction = new QAction(tr("&Make Transaction"), this);
    transactionAction->setStatusTip(tr("Make Transaction"));
    transactionAction->setShortcut(tr("Ctrl+T"));
    connect(transactionAction, SIGNAL(triggered()), this, SLOT(transaction()));

    aboutAction = new QAction(tr("&About Program"), this);
    aboutAction->setStatusTip(tr("About"));
    aboutAction->setShortcut(tr("F4"));
    connect(aboutAction, SIGNAL(triggered()), this, SLOT(about()));

    exitAction = new QAction(tr("&Exit"), this);
    exitAction->setStatusTip(tr("Exit from program"));
    exitAction->setShortcut(tr("Ctrl+Q"));
    connect(exitAction, SIGNAL(triggered()), this, SLOT(close()));

    showHistoryAction = new QAction(tr("&Show History"), this);
    showHistoryAction->setStatusTip(tr("Show History"));
    showHistoryAction->setShortcut(tr("Ctrl+H"));
    connect(showHistoryAction, SIGNAL(triggered()), this, SLOT(showHistory()));

    showAccountsAction = new QAction(tr("&Show Accounts"), this);
    showAccountsAction->setStatusTip(tr("Show Accounts"));
    showAccountsAction->setShortcut(tr("Ctrl+A"));
    connect(showAccountsAction, SIGNAL(triggered()), this, SLOT(showAccounts()));

    fileMenu = menuBar()->addMenu(tr("&Menu"));
    fileMenu->addAction(newAccountAction);
    fileMenu->addAction(findUserAction);
    fileMenu->addAction(transactionAction);
    fileMenu->addAction(aboutAction);
    fileMenu->addSeparator();
    fileMenu->addAction(exitAction);

    operationsMenu = menuBar()->addMenu(tr("&Operations"));
    operationsMenu->addAction(showHistoryAction);
    operationsMenu->addAction(showAccountsAction);

    sqlDb = QSqlDatabase::addDatabase("QSQLITE");
    sqlDb.setDatabaseName("accounts");
    sqlDb.setUserName("user");
    sqlDb.setHostName("host");
    sqlDb.setPassword("password");
    if (!sqlDb.open())
    {
        qDebug() << "Cannot open database:" << sqlDb.lastError();
    }
    sqlModel = new QSqlTableModel;
    sqlView = new QTableView;
    createNewAccount = new NewAccount;
    connectToDataBase();
}

MainWindow::~MainWindow()
{

}

NewAccount::NewAccount (QWidget *parent)
    :QDialog(parent)
{
    nameEdit = new QLineEdit(this);
    nameLabel = new QLabel(tr("&Name:"), this);
    nameLabel->setGeometry(9,40,100,25);
    nameEdit->setGeometry(100,40,200,25);
    nameLabel->setBuddy(nameEdit);
    secondNameEdit = new QLineEdit(this);
    secondNameLabel = new QLabel(tr("&Second Name:"), this);
    secondNameLabel->setBuddy(secondNameEdit);
    secondNameLabel->setGeometry(9,70,100,25);
    secondNameEdit->setGeometry(100,70,200,25);
    lastNameEdit = new QLineEdit(this);
    lastNameLabel = new QLabel(tr("&Last Name:"), this);
    lastNameLabel->setBuddy(lastNameEdit);
    lastNameEdit->setGeometry(100,100,200,25);
    lastNameLabel->setGeometry(9,100,100,25);
    balanceEdit = new QLineEdit(this);
    balanceLabel = new QLabel(tr("&Balance:"), this);
    balanceLabel->setGeometry(9,130,100,25);
    balanceEdit->setGeometry(100,130,200,25);
    balanceLabel->setBuddy(balanceEdit);
    addButton = new QPushButton(tr("&Add"), this);
    closeButton = new QPushButton(tr("&Close"), this);
    addButton->setGeometry(9,190,60,20);
    closeButton->setGeometry(160,190,60,20);

    setWindowTitle(tr("Add New Account"));
    connect(addButton, SIGNAL(clicked()), this, SLOT(addNewAccount()));
    connect(closeButton, SIGNAL(clicked()), this, SLOT(accept()));
}

void NewAccount::addNewAccount()
{
    QSqlQuery query;
    QString   str;
    QString strF =
        "INSERT INTO  accounts (name, second_name, last_name, balance) "
            "VALUES('%1', '%2', '%3', '%4');";

    str = strF.arg(nameEdit->text())
        .arg(secondNameEdit->text())
        .arg(lastNameEdit->text())
        .arg(balanceEdit->text());

    query.exec(str);

    nameEdit->clear();
    secondNameEdit->clear();
    lastNameEdit->clear();
    balanceEdit->clear();
}

void NewAccount::accept(QCloseEvent *event)
{
    event->accept();
}

void MainWindow::connectToDataBase()
{
    /*QString str;
    QSqlQuery query;
    str = "CREATE TABLE accounts (account INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR (30), second_name VARCHAR (30), last_name VARCHAR (30), balance INTEGER);";
    query.exec(str);
    str = "CREATE TABLE history (id INTEGER PRIMARY KEY AUTOINCREMENT, account_source INTEGER, account_destination INTEGER, amount INTEGER);";
    query.exec(str);*/

    //quint16 port;

    server = new Server;
    //port = server->serverPort();

    //qDebug("Listening on port %i. Please press Ctrl-C to exit.", port);
}

void MainWindow::findUserAccount()
{
    int account_number = QInputDialog::getInteger(0, "Account", "Find Account");
    this->sqlModel->setTable("accounts");
    QString str;
    QString strF = "account = '%1'";
    str = strF.arg(QString::number(account_number, 10));
    this->sqlModel->setFilter(str);
    this->sqlModel->select();
    this->sqlView->setModel(sqlModel);
    this->sqlView->show();
}

void MainWindow::transaction()
{
    QSqlQuery query;
    QString str;
    QString strF;
    int account_source = QInputDialog::getInteger(0, "Source", "Enter Source Account");
    int account_destination = QInputDialog::getInteger(0, "Destination", "Enter Destination Account");
    int amount = QInputDialog::getInteger(0, "Amount", "Enter Amount");

    strF = "UPDATE accounts SET balance = (SELECT balance+%1 FROM accounts WHERE account = '%2') WHERE account = '%2'";

    str = strF.arg(QString::number(amount, 10))
            .arg(QString::number(account_destination, 10));
    query.exec(str);

    strF = "UPDATE accounts SET balance = (SELECT balance-%1 FROM accounts WHERE account = '%2') WHERE account = '%2'";
    str = strF.arg(QString::number(amount, 10))
            .arg(QString::number(account_source, 10));
    query.exec(str);

    strF =
        "INSERT INTO  history (account_source, account_destination, amount) "
            "VALUES('%1', '%2', '%3');";

    str = strF.arg(QString::number(account_source, 10))
        .arg(QString::number(account_destination, 10))
        .arg(QString::number(amount, 10));

    query.exec(str);
}

void MainWindow::newAccount()
{
    createNewAccount->show();
}

bool MainWindow::askOnClose()
{
    int r = QMessageBox::question(this, tr("Assert"),tr("Do you want to leave this program?"),
                                  QMessageBox::Yes,
                                  QMessageBox::Cancel | QMessageBox::Escape);
    return (r == QMessageBox::Yes);
}

void MainWindow::closeEvent(QCloseEvent *event)
{
    if (askOnClose())
        event->accept();
    else
        event->ignore();
}

void MainWindow::about()
{
    QMessageBox::about(this, tr("About"),tr("<h2>STA Bank Server 1.0</h2>"
                                                  "<p>by Alexander Ashikhmin"));
}

void MainWindow::showAccounts()
{
    this->sqlModel->setTable("accounts");
    this->sqlModel->select();
    this->sqlView->setModel(sqlModel);
    this->sqlView->show();
}

void MainWindow::showHistory()
{
    int account_number = QInputDialog::getInteger(0, "Account", "Find Account");
    this->sqlModel->setTable("history");
    QString str;
    QString strF = "account_source = '%1' OR account_destination = '%1'";
    str = strF.arg(QString::number(account_number, 10));
    this->sqlModel->setFilter(str);
    this->sqlModel->select();
    this->sqlView->setModel(sqlModel);
    this->sqlView->show();
}
