#!/bin/bash
#
# Script to uninstall PrefCount.
#

installDir=@INSTALLDIR@

echo -n Are you sure you would like to uninstall PrefCount [no]:
read userWish
if [ $userWish ]; then
  if [ $userWish != no ]; then
    echo Deleting links and desktop items...
    rm $HOME/bin/prefcount > /dev/null 2>&1
    rm $HOME/Desktop/PrefCount.desktop > /dev/null 2>&1

    echo Deleting the installation directory...
    rm -rf $installDir > /dev/null 2>&1

    echo PrefCount was uninstalled.
  fi
fi


