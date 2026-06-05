#!/bin/bash
# --- Banking Management System Build Script ---
# Requirements: JDK 17+, MySQL, mysql-connector-java-8.x.jar

MYSQL_CONNECTOR="mysql-connector-j-8.3.0.jar"
LIB_DIR="lib"
OUT_DIR="out"
SRC_DIR="src"

mkdir -p $LIB_DIR $OUT_DIR

# Download MySQL connector if not present
if [ ! -f "$LIB_DIR/$MYSQL_CONNECTOR" ]; then
    echo "[*] Downloading MySQL Connector/J..."
    curl -L "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar" \
         -o "$LIB_DIR/$MYSQL_CONNECTOR"
fi

echo "[*] Compiling..."
find $SRC_DIR -name "*.java" > sources.txt
javac -cp "$LIB_DIR/$MYSQL_CONNECTOR" -d $OUT_DIR @sources.txt
rm sources.txt

if [ $? -ne 0 ]; then
    echo "[!] Compilation failed."
    exit 1
fi

echo "[*] Running..."
java -cp "$OUT_DIR:$LIB_DIR/$MYSQL_CONNECTOR" BankingSystem
