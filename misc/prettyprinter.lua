function run(str)
   local code,err,olderr
   code, err = loadstring("return ("..str..")","input")
   if code then
      show(code())
   else
      olderr = err
      code,err = loadstring(str, "input")
      if code then
	 show(code())
      else
	 print(err)
      end
   end
end

-- Pretty-print a Lua object
function show(x)
   print(pretty(x,"",0,5,{[x]=true}))
end

-- whether a string is a Lua identifier
function is_ident(s)
   return string.find(tostring(s), "^[_%a][_%w]*$")
end

-- FIXME:
-- * limit length of long lists
-- * wrap elements of short lists better (e.g. {1,2,3,...})
-- * put "..." in more sensible positions
function pretty(x, indent, depth, maxdepth, recurselist)
   if depth > maxdepth then return "..." end
   local ty = type(x)
   if ty == "table" then
      local newindent = indent .. "  "
      local function pr(v)
	 local ret
	 if recurselist then
	    if recurselist[v] then
	       ret = "<recursion on "..tostring(v)..">"
	    else
	       recurselist[v] = true
	       ret = pretty(v,newindent, depth+1, maxdepth, recurselist)
	       recurselist[v] = nil
	    end
	 else
	    ret = pretty(v,newindent, depth +1, maxdepth, recurselist)
	 end
	 return ret
      end
      local seen = {}
      local intvals, restkeys, restvals  = {}, {}, {}
      for k, v in ipairs(x) do
	 intvals[#intvals + 1] = pr(v)
	 seen[k] = true
      end
      for k, v in pairs(x) do
	 if not seen[k] then
	    restkeys[#restkeys + 1] = {pr(k), k}
	 end
      end
      table.sort(restkeys, function(a,b) return a[1] < b[1] end)
      for i = 1,#restkeys do
	 restvals[i] = pr(x[restkeys[i][2]])
      end
      local oneline = true
      local linelength, maxlen = #indent + 1, 0
      for i = 1,#intvals do
	 if string.find(intvals[i], "\n", 1, true) then
	    oneline = false
	    break
	 else
	    linelength = linelength + #intvals[i] + 1
	    if #intvals[i] > maxlen then
	       maxlen = #intvals[i]
	    end
	 end
      end
      if maxlen > 10 and linelength > 80 then
	 oneline = false
      end
      local res = {}
      local function out(x) res[#res+1] = x end
      out("{") -- placeholder
      local trailer = "}"
      if #intvals ~= 0 then
	 if oneline then
	    if #restvals ~= 0 then
	       res[1] = "{\n"..newindent
	    else
	       res[1] = "{"
	    end
	    for i = 1,#intvals do
	       out(intvals[i])
	       out(",")
	    end
	    trailer = "}"
	 else
	    res[1] = "{\n"
	    for i = 1,#intvals do
	       out(newindent .. intvals[i])
	       out(",\n")
	    end
	    trailer = "\n" .. indent .. "}"
	 end
	 if #restvals ~= 0 then
	    res[#res] = ","
	 else
	    res[#res] = nil
	 end
      end
      if #restvals ~= 0 then
	 out("\n")
	 for i = 1,#restvals do
	    local prk, k = unpack(restkeys[i])
	    if type(k) == "string" and is_ident(k) then
	       out(newindent .. k .. " = ")
	    else
	       out(newindent .. "[" .. prk .. "] = ")
	    end
	    out(restvals[i])
	    out(",\n")
	 end
	 res[#res] = nil
	 trailer = "\n" .. indent .. "}"
      end
      out(trailer)
      return table.concat(res)
   elseif ty == "string" then
      x = x:gsub([[\]],[[\\]])
      for orig, repl in pairs{
	 ["\a"]="a",
	 ["\b"]="b",
	 ["\f"]="f",
	 ["\n"]="n",
	 ["\r"]="r",
	 ["\t"]="t",
	 ["\v"]="v",
	 ['"'] = '"'} do
	 x = x:gsub(orig, [[\]]..repl)
      end
      return [["]] .. x .. [["]]
   elseif ty == "number" then
      return tostring(x)
   else
      return "<" .. tostring(x) .. ">"
   end
end