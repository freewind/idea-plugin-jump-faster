Idea Plugin Jump Faster
=======================

This IDEA plugin makes us jump to left/right/up/down a little faster (than `option + left/right`)

In this plugin, I defined words contains only such characters: `a-zA-Z0-9_$`, all others are treated as _blank_ chars when jumping.

For keyshorts are provided (and you can change it too, for me, I just use `option + left/right/up/down`):

- `ctrl + option + left`: Jump to previous word, skip all non word chars
- `ctrl + option + right`: Jump to next word, skip all non word chars
- `ctrl + option + up`: Jump to previous line, skip lines not contain any word
- `ctrl + option + down`: Jump to next line, skip lines not contain any word

For now, you can just clone the plugin and build it manually.

Run plugin in IDEA:

```
./gradlew runIde
```

Build the plugin to a zip
-------------------------

```
./gradlew buildPlugin
```

It will generate the plugin file in `./build/distributions`

Notice
-------

- Use `version 'IC-2018.1.5'` every time to reduce download. (about 500M)
- Don't forget to change `id` and `name` in `resources/META-INF/plugin.xml` to current project