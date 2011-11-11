#include "sslserver.h"
//#include <qdir.h>
//#include <qfileinfo.h>
//#include <qdatetime.h>
//#include <QSslCertificate>
//#include <QMessageBox.h>
//#include <QSslSocket>

Server::Server(QObject *parent)
    : QTcpServer(parent)
{
    quint16 port;

    listen(QHostAddress::Any, 45000);
    port = this->serverPort();
    qDebug("Listening on port %i. Please press Ctrl-C to exit.", port);
    if (!isListening())
        qDebug("Can't start the server!");
}

void Server::incomingConnection(int socket)
{
    qDebug("New ServerConnection");
    new SSLServerConnection(socket, this);
}

SSLServerConnection::SSLServerConnection(quint16 socketDescriptor,
                                         QObject *parent)
    : QObject(parent)
{
    //QFile *s_cert = new QFile("certificate.pem");
    //QFile *pr_key = new QFile("private.key");
    //certif = QSslCertificate(s_cert);
    //s_key = QSslKey(pr_key, QSsl::Rsa);
    //socket = new QSslSocket(this);
    socket = new QTcpSocket(this);


    socket->setSocketDescriptor(socketDescriptor);
    //socket->setLocalCertificate(certif);
    //socket->setPrivateKey(s_key);

    connect(socket, SIGNAL(readyRead()), this, SLOT(acceptedClient()));
    connect(socket, SIGNAL(disconnected()), this, SLOT(connectionClosed()));
    connect(socket, SIGNAL(error(QAbstractSocket::SocketError)), SLOT(error(QAbstractSocket::SocketError)));
    connect(parent, SIGNAL(newConnection()), this, SLOT(server_handleConnection()));
    //connect(socket, SIGNAL(encrypted()), this, SLOT(ready()));
    //socket->startServerEncryption();

}

void SSLServerConnection::ready()
{
     // сюда никогда не заходит
 }

 void SSLServerConnection::server_handleConnection()
 {
     qDebug("Connection handle");
     QString t = "Welcome to Fake-DOS 2.11\r\nC:\\>";
     socket->write(t.toLatin1().constData(), t.length());
 }

SSLServerConnection::~SSLServerConnection()
{
    //qDebug("Connection closed.");
}

void SSLServerConnection::acceptedClient()
{
    qDebug("New client!");
    char s[50];
    socket->read(s, 50);
    qDebug(s);
}

void SSLServerConnection::connectionClosed()
{
    if (socket->state() == QAbstractSocket::ClosingState) {
        connect(socket, SIGNAL(disconnected()), SLOT(deleteLater()));
    } else {
        deleteLater();
        return;
    }

    qDebug("Connection closed.");
}

void SSLServerConnection::error(QAbstractSocket::SocketError)
{
    qDebug("Error: %s", qPrintable(socket->errorString()));
}

