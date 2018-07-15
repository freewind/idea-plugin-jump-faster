Idea Plugin Jump Faster
=======================

This IDEA plugin makes us jump to left/right/up/down a little faster (than `option + left/right`)

In this plugin, I defined words contains only such characters: `a-zA-Z0-9_$`, all others are treated as _blank_ chars when jumping.

For keyshorts are provided only for Mac keymaps(`Mac OS X` and `Mac OS X 10.5+`), you can define or change them manually:

- `ctrl + option + left`: Jump to previous word, skip all non word chars
- `ctrl + option + right`: Jump to next word, skip all non word chars
- `ctrl + option + up`: Jump to previous line, skip lines not contain any word
- `ctrl + option + down`: Jump to next line, skip lines not contain any word

If the keyshorts are not working, please set them manually for now (I'll fix this)

For now, you can just clone the plugin and build it manually, I will publish it to IDEA plugin site soon.

Build the plugin to a zip
-------------------------

```
./gradlew buildPlugin
```

It will generate the plugin file in `./build/distributions`

Run plugin in IDEA for Dev
--------------------------

```
./gradlew runIde
```


Notice
-------

- Use `version 'IC-2018.1.5'` every time to reduce download. (about 500M)
- Don't forget to change `id` and `name` in `resources/META-INF/plugin.xml` to current project