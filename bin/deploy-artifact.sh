mvn --batch-mode --errors release:prepare



cat << EOF >> "${BITBUCKET_CLONE_DIR}/build.properties"
module.name=${BITBUCKET_REPO_SLUG}
module.version=${RELEASE_VERSION}
author.email=${LAST_COMMIT_AUTHOR_EMAIL}
EOF
