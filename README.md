Spothole Android App
=====================

# Notes

## APK release signing key

In order to build the release APK, you will need to get a hold of the keystore and the
corresponding password and place them in your home directory like so:

```
~/.spothole
└── [drwx------]  keys
    ├── [-rw-------]  password
    └── [-rw-------]  spothole-key.keystore
```

 * `password`: contains the password to both keystore and key
 * `spothole-key.keystore`: the keystore itself

## Dependencies

I had to install the following libraries on my laptop in order 
to be able to build this App.

Laptop Specs:
 * x86_64 processor
 * Fedora 19 (64 bit)

```sh
sudo yum install glibc.i686 zlib-devel zlib.i686 libstdc++.i686
```
