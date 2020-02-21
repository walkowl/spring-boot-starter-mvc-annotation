#!/bin/bash

sed -i'bak' "/<servers>/ a\
<server><id>maven-atlassian-com</id><username>${atlassian_private_username}</username><password>${atlassian_private_password}</password></server>" /usr/share/maven/conf/settings.xml

sed -i'bak' "/<servers>/ a\
<server><id>atlassian-private</id><username>${atlassian_private_username}</username><password>${atlassian_private_password}</password></server>" /usr/share/maven/conf/settings.xml

sed -i'bak' '/<profiles>/ a\
<profile><id>atlassian-private</id><activation><activeByDefault>true</activeByDefault></activation><repositories><repository><id>atlassian-private</id><name>Atlassian Private</name><url>https://packages.atlassian.com/maven/private</url><layout>default</layout></repository><repository><id>maven-atlassian-com</id><name>Atlassian Private Repository</name><url>https://packages.atlassian.com/maven/atlassian-private</url><layout>default</layout></repository></repositories><pluginRepositories><pluginRepository><id>atlassian-private</id><url>https://packages.atlassian.com/maven/repository/internal</url></pluginRepository></pluginRepositories></profile>' /usr/share/maven/conf/settings.xml

sed -i'bak' '/<profiles>/ a\
<profile><id>atlassian-public</id><activation><activeByDefault>true</activeByDefault></activation><repositories><repository><id>atlassian-public</id><url>https://packages.atlassian.com/maven/repository/public</url></repository></repositories></profile>' /usr/share/maven/conf/settings.xml