#!/bin/bash
#
# Saved as its own file so that Travis uses the correct shell to run it.
echo Create node_module/rn-snackbar
mkdir node_modules
mkdir node_modules/@g33n
mkdir node_modules/@g33n/rn-snackbar
echo Copy necesary files to example project
cp -R ../{package.json,android,ios,lib} node_modules/@g33n/rn-snackbar/
