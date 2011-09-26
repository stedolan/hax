As well as the above silliness, I do from time to time produce
programs that do something worth doing. I'm hiding them here in case
anyone finds out.


## sshbounce

sshbounce is a script to automate something I've spent waaaay too much
of my time doing: jury-rigging SSH tunnels to log into a machine
behind a few layers of firewall. Suppose you want to SSH into your
desktop machine at home, but the only machine you can directly connect
to is the firewall. You can write:

    sshbounce myself@firewallbox myself@homemachine

and it will set up an SSH tunnel so that the next time you type `ssh
homemachine` it will log you in instantly, without requiring a
password (via the SSH's underused-but-awesome control socket feature).
