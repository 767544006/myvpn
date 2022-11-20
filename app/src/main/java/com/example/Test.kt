//proto udp
//explicit-exit-notify
//remote
//dev tun
//resolv-retry infinite
//nobind
//persist-key
//persist-tun
//remote-cert-tls server
//verify-x509-name server_R2Puof1cldz4yD3X name
//auth SHA256
//auth-nocache
//cipher AES-128-GCM
//tls-client
//tls-version-min 1.2
//tls-cipher TLS-ECDHE-ECDSA-WITH-AES-128-GCM-SHA256
//ignore-unknown-option block-outside-dns
//setenv opt block-outside-dns # Prevent Windows 10 DNS leak
//verb 3
//
//
//
//
//dev tun
//proto udp
//remote 54.215.197.190 1194
//resolv-retry infinite
//nobind
//persist-key
//persist-tun
//remote-cert-tls server
//auth SHA512
//cipher AES-256-CBC
//ignore-unknown-option block-outside-dns
//verb 3
