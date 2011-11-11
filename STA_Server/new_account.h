#ifndef NEW_ACCOUNT_H
#define NEW_ACCOUNT_H

#include <QtGui/QApplication>
#include <QtGui/QMainWindow>
#include <QtGui>
#include <QTableView>
#include <set>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

class NewAccount : public QDialog
{
    Q_OBJECT
    public:
        NewAccount(QWidget *parent = 0);

    private slots:
        void addNewAccount();

    private:
        QLabel *nameLabel;
        QLabel *secondNameLabel;
        QLabel *lastNameLabel;
        QLabel *balanceLabel;
        QDialogButtonBox *buttonBox;
        QPushButton *addButton;
        QPushButton *closeButton;
        QLineEdit *nameEdit;
        QLineEdit *secondNameEdit;
        QLineEdit *lastNameEdit;
        QLineEdit *balanceEdit;

    protected:
        virtual void accept(QCloseEvent *event);


};

#endif // NEW_ACCOUNT_H

