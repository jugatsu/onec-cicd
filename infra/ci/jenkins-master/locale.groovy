println("--- Configuring Locale")

def pluginWrapper = jenkins.model.Jenkins.instance.getPluginManager().getPlugin('locale')
def plugin = pluginWrapper.getPlugin()

plugin.setSystemLocale('en')
plugin.ignoreAcceptLanguage = true
