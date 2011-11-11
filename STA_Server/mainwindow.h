#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QtGui/QApplication>
#include <QtGui/QMainWindow>
#include <QtGui>
#include <QtSql>
#include <QSqlQuery>
#include <QTableView>
#include <set>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "new_account.h"
#include "sslserver.h"

const int WinWidth = 640;
const int WinHeight = 480;

class MainWindow : public QMainWindow
{
    Q_OBJECT

protected:
    virtual void closeEvent(QCloseEvent *event);

private slots:
    void connectToDataBase();
    void newAccount();
    void findUserAccount();
    void transaction();
    void about();

    void showHistory();
    void showAccounts();

private:
    QSqlDatabase sqlDb;
    Server* server;
    NewAccount* createNewAccount;
    QSqlTableModel* sqlModel;
    QTableView* sqlView;
    QMenu   *fileMenu;
    QAction *newAccountAction;
    QAction *findUserAction;
    QAction *transactionAction;
    QAction *aboutAction;
    QAction *exitAction;

    QMenu   *operationsMenu;
    QAction *showHistoryAction;
    QAction *showAccountsAction;

    bool askOnClose();

public:
    MainWindow(QWidget *parent = 0);
    ~MainWindow();
};

#endif // MAINWINDOW_H
