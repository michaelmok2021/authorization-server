#!/bin/bash

# use: certbot renew --post-hook /usr/local/bin/certbot-renew-fix-file-access.sh

chmod 0755 /etc/letsencrypt/
chmod 0711 /etc/letsencrypt/live/
chmod 0750 /etc/letsencrypt/live/sso.billview.com.au/
chmod 0711 /etc/letsencrypt/archive/
chmod 0750 /etc/letsencrypt/archive/sso.billview.com.au/
chmod 0640 /etc/letsencrypt/archive/sso.billview.com.au/{cert,chain,fullchain}*.pem
chmod 0640 /etc/letsencrypt/archive/sso.billview.com.au/privkey*.pem

chown root:root /etc/letsencrypt/
chown root:root /etc/letsencrypt/live/
chown root:ubuntu /etc/letsencrypt/live/sso.billview.com.au/
chown root:root /etc/letsencrypt/archive/
chown root:ubuntu /etc/letsencrypt/archive/sso.billview.com.au/
chown root:ubuntu /etc/letsencrypt/archive/sso.billview.com.au/{cert,chain,fullchain}*.pem
chown root:ubuntu /etc/letsencrypt/archive/sso.billview.com.au/privkey*.pem

# /etc/init.d/nginx restart
