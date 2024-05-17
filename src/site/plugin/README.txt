If you maven-site-plugin does not support Russian locale,
follow the given below steps to install Russian locale:

1. Checkout maven site plugin. It might be here:
   http://svn.apache.org/repos/asf/maven/plugins/tags/maven-site-plugin-2.0-beta-6
   , but you might need to check if this is the tag you need to use;

2. Copy the Russian resource file (site-plugin_ru.properties)
   to src/main/resources;

3. Run mvn install on the plugin.
