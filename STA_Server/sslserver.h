#ifndef SSLSERVER_H
#define SSLSERVER_H
//#include <qtcpserver.h>
//#include <QSslSocket>
#include <QtNetwork>
#include <stdlib.h>
#include <string.h>
//#include <QSslSocket>
//#include <QSslCertificate>
//#include <QSslKey>

class SSLServerConnection : public QObject
{
   Q_OBJECT

public:
   SSLServerConnection(quint16 socket, QObject *parent = 0);
   ~SSLServerConnection();

public slots:
   void acceptedClient();
   void connectionClosed();
   void error(QAbstractSocket::SocketError err);
   void ready();
   void server_handleConnection();

private:
   unsigned int readBytes;
   unsigned int writtenBytes;

   QTcpSocket *socket;
   //QSslSocket *socket;
   //QSslCertificate certif;
   //QSslKey s_key;

};

class Server : public QTcpServer
{
   Q_OBJECT

public:
   Server(QObject *parent = 0);
   //quint16 port;
   void incomingConnection(int socket);
};

#endif
