#!/bin/bash
#
# Script to install the PrefCount application.
# This will copy the applicaiton files to the
# chosen destination and will create link
# to the executable.
#

installPath=/usr/local/prefcount

echo This script will install the PrefCount application onto your system.

echo -n Please, specify the installation path [$installPath]:

# asking user to enter the installation path
read installPathUser
if [ $installPathUser ]; then
  installPath=$installPathUser
fi

# preparing the installation directory
if [ -d $installPath ]; then
  echo Directory \"$installPath\" already exists - cleaning it...
  # we are not deleting the whole $installPath just to be cautious - accidents happen
  rm -rI $installPath/bin
  rm -rf $installPath/files
  rm $installPath/uninstall 
else
  echo Directory \"$installPath\" does not exist - creating it...
  mkdir $installPath 
fi

# copying files into the installation directory
cp -pr README* PrefCount-license.txt bin files $installPath/

# creating executable links and desktop files
chmod 755 $installPath/bin/*.sh
rm $HOME/bin/prefcount > /dev/null 2>&1 
ln -s $installPath/bin/run.sh $HOME/bin/prefcount
ln -s $installPath/bin/uninstall.sh $installPath/uninstall

# need to replace the installation directory in the scripts
awk -v path=$installPath '{ gsub(/@INSTALLDIR@/,path); print }' bin/run.sh > $installPath/bin/run.sh
awk -v path=$installPath '{ gsub(/@INSTALLDIR@/,path); print }' bin/uninstall.sh > $installPath/bin/uninstall.sh
awk -v path=$installPath '{ gsub(/@INSTALLDIR@/,path); print }' files/PrefCount.desktop > $installPath/files/PrefCount.desktop

# putting the desktop link on the user desktop
cp $installPath/files/PrefCount.desktop $HOME/Desktop/



