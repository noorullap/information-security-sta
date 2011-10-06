#include "mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
{
    resize(WinWidth, WinHeight);

    runAction = new QAction(tr("&Run"), this);
    runAction->setStatusTip(tr("Run"));
    runAction->setShortcut(tr("F1"));
    connect(runAction, SIGNAL(triggered()), this, SLOT(run()));

    optionAction = new QAction(tr("&Options"), this);
    optionAction->setStatusTip(tr("Options"));
    optionAction->setShortcut(tr("F2"));
    connect(optionAction, SIGNAL(triggered()), this, SLOT(option()));

    openAction = new QAction(tr("&Open Account File"), this);
    openAction->setStatusTip(tr("Open"));
    openAction->setShortcut(tr("Ctrl+O"));
    connect(openAction, SIGNAL(triggered()), this, SLOT(open()));

    saveAction = new QAction(tr("&Save Account File"), this);
    saveAction->setStatusTip(tr("Save"));
    saveAction->setShortcut(tr("Ctrl+S"));
    connect(saveAction, SIGNAL(triggered()), this, SLOT(save()));

    aboutAction = new QAction(tr("&About Program"), this);
    aboutAction->setStatusTip(tr("About"));
    aboutAction->setShortcut(tr("F4"));
    connect(aboutAction, SIGNAL(triggered()), this, SLOT(about()));

    exitAction = new QAction(tr("&Exit"), this);
    exitAction->setStatusTip(tr("Exit from program"));
    exitAction->setShortcut(tr("Ctrl+Q"));
    connect(exitAction, SIGNAL(triggered()), this, SLOT(close()));

    fileMenu = menuBar()->addMenu(tr("&Menu"));
    fileMenu->addAction(runAction);
    fileMenu->addAction(optionAction);
    fileMenu->addAction(openAction);
    fileMenu->addAction(saveAction);
    fileMenu->addAction(aboutAction);
    fileMenu->addSeparator();
    fileMenu->addAction(exitAction);
}

MainWindow::~MainWindow()
{

}

void MainWindow::run()
{
    QSqlDatabase db = QSqlDatabase::addDatabase("QODBC");
            db.setDatabaseName("sq");
            db.setUserName("ali");
            if (true == db.open()){
               QMessageBox box;
                    box.setText("Connection is open!");
                    box.exec();
            }
             else
             {
                    QMessageBox box;
                    box.setText("Connection is close!");
                    box.exec();
             }
}

void MainWindow::open()
{
}

void MainWindow::save()
{
}

void MainWindow::option()
{
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
