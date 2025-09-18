import tkinter as tk
from tkinter import ttk, messagebox
 
# Dictionary of 100 Linux commands with descriptions
commands = {
    "ls": "List directory contents",
    "pwd": "Print working directory",
    "cd": "Change directory",
    "mkdir": "Create a new directory",
    "rmdir": "Remove an empty directory",
    "touch": "Create an empty file",
    "rm": "Remove files or directories",
    "cp": "Copy files or directories",
    "mv": "Move or rename files or directories",
    "cat": "Display file contents",
    "more": "View file contents page by page",
    "less": "View file contents with navigation",
    "head": "Show first 10 lines of a file",
    "tail": "Show last 10 lines of a file",
    "echo": "Display a line of text",
    "man": "Show manual pages",
    "which": "Locate a command",
    "whereis": "Locate binary, source, and man page of a command",
    "find": "Search for files in a directory hierarchy",
    "locate": "Find files by name",
    "grep": "Search text using patterns",
    "wc": "Word, line, character, and byte count",
    "sort": "Sort lines of text",
    "uniq": "Report or omit repeated lines",
    "diff": "Show differences between two files",
    "cmp": "Compare two files byte by byte",
    "tar": "Archive files",
    "gzip": "Compress files",
    "gunzip": "Decompress files",
    "zip": "Compress files into a zip archive",
    "unzip": "Extract files from a zip archive",
    "df": "Show disk space usage",
    "du": "Show file space usage",
    "free": "Show memory usage",
    "top": "Display running processes",
    "htop": "Interactive process viewer",
    "ps": "Show process status",
    "kill": "Terminate a process",
    "killall": "Terminate processes by name",
    "jobs": "Show background jobs",
    "fg": "Bring a background job to foreground",
    "bg": "Resume a job in the background",
    "chmod": "Change file permissions",
    "chown": "Change file owner",
    "chgrp": "Change file group",
    "stat": "Show file or filesystem status",
    "file": "Determine file type",
    "hostname": "Show system hostname",
    "uname": "Show system information",
    "uptime": "Show system uptime",
    "whoami": "Show current user",
    "id": "Show user identity",
    "groups": "Show user groups",
    "adduser": "Add a new user",
    "deluser": "Delete a user",
    "passwd": "Change user password",
    "su": "Switch user",
    "sudo": "Execute command as another user",
    "date": "Display or set date and time",
    "cal": "Show calendar",
    "clear": "Clear terminal screen",
    "history": "Show command history",
    "alias": "Create command alias",
    "unalias": "Remove command alias",
    "export": "Set environment variables",
    "env": "Show environment variables",
    "printenv": "Print environment variables",
    "ssh": "Remote login via SSH",
    "scp": "Securely copy files between hosts",
    "wget": "Download files from the web",
    "curl": "Transfer data from/to a server",
    "ping": "Send ICMP ECHO_REQUEST to a host",
    "traceroute": "Trace the route packets take to a host",
    "ifconfig": "Configure network interfaces (deprecated, use ip)",
    "ip": "Show or manipulate routing, devices, policy",
    "netstat": "Show network statistics",
    "ss": "Show socket statistics",
    "iptables": "Configure firewall",
    "systemctl": "Control systemd services",
    "service": "Start/stop/restart services",
    "shutdown": "Shutdown the system",
    "reboot": "Reboot the system",
    "mount": "Mount a filesystem",
    "umount": "Unmount a filesystem",
    "lsblk": "List block devices",
    "blkid": "Show block device attributes",
    "dmesg": "Show kernel ring buffer messages",
    "journalctl": "Query systemd logs",
    "crontab": "Schedule periodic tasks",
    "at": "Schedule a task at a specific time",
    "alias": "Create shortcuts for commands",
    "history": "Show previously executed commands",
    "nano": "Text editor",
    "vi": "Text editor",
    "vim": "Advanced text editor",
    "gedit": "GUI text editor",
    "apt-get": "Package manager (Debian/Ubuntu)",
    "yum": "Package manager (CentOS/RHEL)",
    "dnf": "Package manager (Fedora)",
    "snap": "Snap package manager",
    "flatpak": "Flatpak package manager"
}
 
# GUI setup
root = tk.Tk()
root.title("Linux Commands Explorer")
root.geometry("700x500")
 
# Heading
heading = tk.Label(root, text="Linux Commands Explorer", font=("Arial", 16, "bold"))
heading.pack(pady=10)
 
# Search bar
search_var = tk.StringVar()
search_entry = tk.Entry(root, textvariable=search_var, width=40, font=("Arial", 12))
search_entry.pack(pady=5)
 
# Command listbox
listbox = tk.Listbox(root, width=40, height=20, font=("Arial", 12))
for cmd in sorted(commands.keys()):
    listbox.insert(tk.END, cmd)
listbox.pack(side=tk.LEFT, padx=10, pady=10)
 
# Scrollbar
scrollbar = tk.Scrollbar(root)
scrollbar.pack(side=tk.LEFT, fill=tk.Y)
listbox.config(yscrollcommand=scrollbar.set)
scrollbar.config(command=listbox.yview)
 
# Info box
info_box = tk.Text(root, wrap=tk.WORD, width=50, height=20, font=("Arial", 12))
info_box.pack(side=tk.LEFT, padx=10, pady=10)
 
# Function to display command details
def show_command(event=None):
    selection = listbox.curselection()
    if selection:
        cmd = listbox.get(selection[0])
        info_box.delete(1.0, tk.END)
        info_box.insert(tk.END, f"Command: {cmd}\n\n")
        info_box.insert(tk.END, f"Description: {commands[cmd]}\n\n")
        info_box.insert(tk.END, f"Example:\n$ {cmd}")
 
listbox.bind("<<ListboxSelect>>", show_command)
 
# Function to search commands
def search_command():
    query = search_var.get().lower()
    listbox.delete(0, tk.END)
    for cmd in sorted(commands.keys()):
        if query in cmd.lower():
            listbox.insert(tk.END, cmd)
 
search_button = tk.Button(root, text="Search", command=search_command, font=("Arial", 12))
search_button.pack(pady=5)
 
root.mainloop()