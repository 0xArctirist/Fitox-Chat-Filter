# Fitox CF - Chat Filter Plugin Made By Arctirtist

A multi layer chat fitler that does not use AI to detect text.

## 🌟 Features

### Multi Layer Detection System
- **Exact matching** - Direct word detection
- **Fuzzy matching** - Catches misspellings and character substitutions (Levenshtein distance)
- **Repeat collapse** - Detects bypass attempts
- **Context-aware detection** - Smart checking to minimize false positives
- **Euphemism detection** - Catches coded language

### Smart False Positive Prevention
- **Whitelist system** - Allows phrases that might otherwise flag
- **Common word detection** - Recognizes normal conversation patterns
- **Context checking** - Only flags words used in harmful contexts
- **Word boundary checks** - Prevents partial word matches

### Content Categories
- **Slurs** - Racial, ethnic, and discriminatory language
- **Threats** - Death threats, violence, and harmful intent
- **Sexual** - Sexual harassment and inappropriate content
- **Advertising** - Server advertisements and spam

### Additional Features
- **Reputation system** - Tracks player behavior over time
- **Sliding window detection** - Identifies patterns of violations
- **Discord webhook alerts** - Notifies staff of violations

## ⚙️ Configuration

### Discord Webhook Setup

Get notified in Discord when violations occur:

1. Go to your Discord server settings
2. Navigate to **Integrations → Webhooks**
3. Click **New Webhook**
4. Copy the webhook URL
5. Paste it in `config.yml`:

```yaml
discord-webhook:
  url: "https://discord.com/api/webhooks/your_webhook_here"
  username: "Test name"
  avatar-url: "https://your-avatar-url.png"
```

### Word Lists

Word lists are stored in `plugins/ChatFilter/rules/`:
- `slurs.yml` - Racial and discriminatory slurs
- `threats.yml` - Violent threats and harmful language
- `sexual.yml` - Sexual content and harassment
- `advertising.yml` - Server ads and spam phrases

You can edit these files to add or remove words, then use `/cf reload` to apply changes.

### Whitelist

The whitelist prevents false positives by allowing innocent phrases. Edit word lists to add whitelist entries, then reload.

## 🎮 Commands

| Command | Description          
|---------|------------------------
| `/cf` | Shows the cmds        
| `/cf true` | Enables the filter    
| `/cf false` | Disables the filter
| `/cf reload` | Reloads the word lists

## 🛡️ How It Works

### Detection Process

1. **Player sends a message** → Plugin intercepts it
2. **Text normalization** → Removes special characters, converts to lowercase
3. **Multi-layer checking**:
   - Check exact word match
   - Check with repeated letters collapsed
   - Check fuzzy match
   - Check for euphemisms in context
4. **Whitelist verification** → Allow if phrase is whitelisted
5. **Context analysis** → Check if used in harmful context
6. **Action taken**:
   - Block the message
   - Alert staff in Discord
   - Log the violation
   - Update player reputation

## 🔧 Advanced Configuration

### Reputation System

Tracks player behavior over time:
- Increases reputation for clean chat
- Decreases reputation for violations

### Sliding Window Detection

Identifies patterns of violations:
- Tracks violations over time
- Detects repeated offenders
- Helps identify ban worthy behavior

## 📊 Detection Statistics

The plugin tracks:
- Total violations by category
- Violations per player
- Most common bypassed words
- False positive rate (via whitelist usage)

## 🐛 Troubleshooting

### False Positives

If innocent phrases are being flagged:
1. Add them to the whitelist in the appropriate word list file
2. Run `/cf reload` to apply changes
3. Test the phrase again

### Bypasses Getting Through

If inappropriate content is bypassing the filter:
1. Add the bypassed word to the appropriate category file
2. Run `/cf reload` to apply changes
3. Consider adjusting fuzzy match threshold

## ⚠️ Disclaimer

No chat filter is 100% perfect. This filter provides a strong foundation for moderation, but:
- Some false positives may occur
- Creative users may find new bypass methods
- Regular maintenance and updates are recommended
- Human moderation is still important
---

**I'm not the best programmer since I am new to java so the code may be trash xd**
